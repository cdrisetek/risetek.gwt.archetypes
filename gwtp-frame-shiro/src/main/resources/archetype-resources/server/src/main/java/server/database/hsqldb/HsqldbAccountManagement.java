package ${package}.server.database.hsqldb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.IAuthorizingHandler;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

@Singleton
public class HsqldbAccountManagement implements IAccountManagement {
	private final SessionFactory sessionFactory;
	@Inject
	public HsqldbAccountManagement(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		System.out.println("sessionFactory ================= : " + sessionFactory);

		new DevOpsTask("Accounts scaner", TaskType.AUTHOR, t -> {
			// Check anybody get DEVELOPER role.
			if(anyMatchedRole("DEVELOPER"))
				t.stat = TaskState.READY;
			else {
				t.stat = TaskState.FAILED;
				t.addMessage("Without basic authorization.");
			}
		});
	}

	private boolean isLike(AccountEntity entity, String like) {
		if(null == like || entity.getPrincipal().indexOf(like) != -1)
			return true;
		
		Map<String, String> description = entity.getDescriptions();
		
		if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
			return true;

		return false;
	}

	private List<AccountEntity> _readAccounts(String like, int offset, int limit) throws SQLException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();

		List<AccountEntity> query = session.createQuery("FROM AccountEntity", AccountEntity.class).getResultList();
		transaction.commit();

		List<AccountEntity> list = new Vector<>();
		int start = 0, count = 0;
		for(AccountEntity e:query) {
			if(!isLike(e, like) || start++ < offset)
				continue;
			
			if(count++ >= limit)
				break;

			list.add(e);
		}
		return list;
	}

	private List<AccountEntity> _readAccountsE(Set<AccountEntity> entities) throws SQLException {
		List<AccountEntity> list = new Vector<>();
		
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		for(AccountEntity entity:entities) {
			session.bySimpleNaturalId(AccountEntity.class).loadOptional(entity.principal).ifPresent(e -> list.add(e));
		}
		transaction.commit();
		
		return list;
	}

	@Override
	public List<AccountEntity> readAccounts(Set<AccountEntity> entities, String like, int offset, int size)
			throws ActionException {
		
		try {
			if(null == entities)
				return _readAccounts(like, offset, size);
			else
				return _readAccountsE(entities);
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}	
	
	@Override
	public void updateAccounts(Set<AccountEntity> subjects) throws ActionException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		for(AccountEntity entity:subjects) {
			AccountEntity account = session.bySimpleNaturalId(AccountEntity.class).load(entity.principal);
			if(null == account)
				throw new ActionException("Invalid principal: " + entity.principal);
			account.descriptions = entity.descriptions;
			session.saveOrUpdate(account);
		}
		transaction.commit();
	}

	@Inject private IAuthorizingHandler authorizing;
	@Override
	public void createAccount(AccountEntity account, String password) throws ActionException {
		try(Session session = sessionFactory.getCurrentSession();) {
			account.setPassword(authorizing.encryptRealmPassword(password));
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.save(account);
			transaction.commit();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getCredential(Object principal) throws ActionException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		AccountEntity account = session.bySimpleNaturalId(AccountEntity.class).load(principal);
		transaction.commit();
		return account.password;
	}

	@Override
	public void setCredential(Object principal, Object credential) throws ActionException {
		System.out.println("setCredential");

		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		AccountEntity account = session.bySimpleNaturalId(AccountEntity.class).load(principal);
		if(null == account)
			throw new ActionException("Invalid principal: " + principal);
		account.password = authorizing.encryptRealmPassword((String)credential);
		session.saveOrUpdate(account);
		transaction.commit();
		
	}

	@Override
	public AccountEntity getAccount(Object principal) throws ActionException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		AccountEntity account = session.bySimpleNaturalId(AccountEntity.class).load(principal);
		transaction.commit();
		return account;
	}

	@Inject private IRoleManagement roleManagement;
	@Override
	public boolean anyMatchedRole(String role) {
		System.out.println("Check roles==========================================");
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		Query<AccountEntity> query = session.createQuery("FROM AccountEntity", AccountEntity.class);
		List<AccountEntity> list =  query.getResultList();
		System.out.println("Check roles 2: " + list);
		
		boolean result = list.stream().anyMatch(e -> {
			try {
				return roleManagement.getRoleSet(e.principal, null).stream().anyMatch(r -> r.equals(role));
			} catch (ActionException e1) {
				e1.printStackTrace();
				return false;
			}});
		System.out.println("Check roles3: " + result);

		transaction.commit();
		return result;
	}
}
