package ${package}.server.database.hsqldb;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;
import ${package}.share.templates.Project;

@Singleton
public class HsqldbRoleManagement implements IRoleManagement {
	private final SessionFactory sessionFactory;

	@Inject
	public HsqldbRoleManagement(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		DevOpsTask task = new DevOpsTask("Hsqldb RoleManagement", TaskType.FATAL, null);
		task.addMessage("connection and statements passed");
		task.stat = TaskState.READY;
	}

	@Override
	public Set<String> getRoleSet(Object principal, Object project) throws ActionException {
		String key = makeKey(principal, project);
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		RoleEntity roleEntity = session.byId(RoleEntity.class).load(key);
		transaction.commit();
		if(null == roleEntity)
			return null;
		return roleEntity.getRoleSet();
	}

	@Override
	public void setRoleSet(Object principal, Object project, Set<String> roles) throws ActionException {
		String key = makeKey(principal, project);
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		RoleEntity roleEntity = session.byId(RoleEntity.class).load(key);
		if(null == roleEntity) {
			roleEntity = new RoleEntity();
			roleEntity.principal = (String)principal;
			roleEntity.project = (String)project;
			roleEntity.key = key;
		}

		roleEntity.addRoleSet(roles);
		session.saveOrUpdate(roleEntity);
		transaction.commit();
	}

	@Override
	public Set<HostProjectRBAC> getRoleSet(Object principal) throws ActionException {
		return getRoleSet(principal, null).stream().map(r -> HostProjectRBAC.valueOf(r)).collect(Collectors.toSet());
	}

	@Override
	public void setRoleSet(Object principal, Set<HostProjectRBAC> roles) throws ActionException {
		setRoleSet(principal, null, roles.stream().map(r -> r.name()).collect(Collectors.toSet()));
	}
	
	/**
	 * 
	 * @param principal
	 * @param project
	 * @return if principal null, return value start 'P'
	 *         if project is null, return value start 'H'
	 *         or return start from 'X' 
	 */
	private String makeKey(Object principal, Object project) {
		assert (null != principal || null != project) : "invalid paramter";
		String key = (principal == null ? "{}":principal) + (project == null ? ("L" + Project.name) : ("R" + project));
		key = Integer.toHexString(key.hashCode());
		if(null == principal)
			return "P" + key;
		else if(null == project)
			return "H" + key;
		else
			return "X" + key;
	}
}
