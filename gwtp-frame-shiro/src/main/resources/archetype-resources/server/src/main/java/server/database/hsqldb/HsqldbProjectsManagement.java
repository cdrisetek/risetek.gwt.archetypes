package ${package}.server.database.hsqldb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IProjectsManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

@Singleton
public class HsqldbProjectsManagement implements IProjectsManagement {
	private final SessionFactory sessionFactory;

	@Inject
	public HsqldbProjectsManagement(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		DevOpsTask task = new DevOpsTask("Hsqldb ProjectsManagement", TaskType.FATAL, null);
		task.addMessage("connection and statements passed");
		task.stat = TaskState.READY;
	}

	private boolean isLike(ProjectEntity entity, String like) {
		if(null == like || entity.getName().indexOf(like) != -1)
			return true;
		
		Map<String, String> description = entity.getDescriptions();
		
		if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
			return true;

		return false;
	}

	private List<ProjectEntity> _readProjects(String like, int offset, int limit) throws SQLException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();

		List<ProjectEntity> query = session.createQuery("FROM ProjectEntity", ProjectEntity.class).getResultList();
		transaction.commit();

		List<ProjectEntity> list = new Vector<>();
		int start = 0, count = 0;
		for(ProjectEntity e:query) {
			if(!isLike(e, like) || start++ < offset)
				continue;
			
			if(count++ >= limit)
				break;

			list.add(e);
		}
		return list;
	}

	private List<ProjectEntity> _readProjectsE(Set<ProjectEntity> entities) throws SQLException {
		List<ProjectEntity> list = new Vector<>();
		
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		for(ProjectEntity entity:entities) {
			session.bySimpleNaturalId(ProjectEntity.class).loadOptional(entity.getName()).ifPresent(e -> list.add(e));
			// session.byId(ProjectEntity.class).loadOptional(entity.getName()).ifPresent(e -> list.add(e));
		}
		transaction.commit();
		
		return list;
	}
	
	@Override
	public List<ProjectEntity> readProjects(Set<ProjectEntity> projects, String like, int offset, int limit)
			throws ActionException {
		try {
			if(null == projects)
				return _readProjects(like, offset, limit);
			else
				return _readProjectsE(projects);
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}

	@Override
	public void updateOrInsert(Set<ProjectEntity> entities) throws ActionException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		for(ProjectEntity entity:entities) {
			// ProjectEntity account = session.byId(ProjectEntity.class).load(entity.getName());
			ProjectEntity account = session.bySimpleNaturalId(ProjectEntity.class).load(entity.getName());
			if(null == account)
				session.save(entity);
			else {
				session.clear();
				account.setDescription(entity.getDescriptions());
				session.saveOrUpdate(account);
			}
		}
		transaction.commit();
	}

	@Transactional
	@Override
	public void clearDatas() throws ActionException {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		session.createNativeQuery("DELETE FROM ProjectEntity").executeUpdate();
		transaction.commit();
	}
}
