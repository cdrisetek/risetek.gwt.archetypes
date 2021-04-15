package ${package}.server.database.hsqldb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Singleton;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IProjectsManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

@Singleton
public class HsqldbProjectsManagement implements IProjectsManagement {

	public HsqldbProjectsManagement() {
		DevOpsTask task = new DevOpsTask("Hsqldb ProjectsManagement", TaskType.FATAL, null);
		try {
			projectSelectStatement = DatabaseManagement.database.getConnection().prepareStatement(projectsSelectSql);
			selectEntityStatement = DatabaseManagement.database.getConnection().prepareStatement(selectEntitySql);
			projectUpsetStatement = DatabaseManagement.database.getConnection().prepareStatement(projectsUpsertSql);
			task.addMessage("connection and statements passed");
			task.stat = TaskState.READY;
		} catch (SQLException e) {
			task.addMessage("test SQL failed");
			task.stat = TaskState.FAILED;
		}
	}

	private boolean isLike(ProjectEntity entity, String like) {
		if(null == like || entity.getName().indexOf(like) != -1)
			return true;
		
		Map<String, String> description = entity.getDescriptions();
		
		if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
			return true;

		return false;
	}

	private PreparedStatement projectSelectStatement;
	private final String projectsSelectSql = "SELECT NAME, DESCRIPTIONS FROM PROJECTS";
	private List<ProjectEntity> _readProjects(String like, int offset, int limit) throws SQLException {
		List<ProjectEntity> list = new Vector<>();

		int start = 0, count = 0;
		try (ResultSet resultSet = projectSelectStatement.executeQuery()) {
			while(resultSet.next()) {
				ProjectEntity entity = new ProjectEntity();
				entity.setName(resultSet.getString(1));
				@SuppressWarnings("unchecked")
				Map<String, String> descMap = (Map<String, String>)resultSet.getObject(2);
				entity.setDescription(descMap);

				if(!isLike(entity, like) || start++ < offset)
					continue;
				
				if(count++ >= limit)
					break;

				list.add(entity);
			}
			DatabaseManagement.statisticsRead();
		} catch (SQLException e) {
			throw e;
		}

		return list;
	}

	private PreparedStatement selectEntityStatement;
	private final String selectEntitySql = "SELECT NAME, DESCRIPTIONS FROM PROJECTS WHERE NAME=?";
	private List<ProjectEntity> _readProjectsE(Set<ProjectEntity> entities) throws SQLException {
		List<ProjectEntity> list = new Vector<>();

		for(ProjectEntity entity:entities) {
			selectEntityStatement.setString(1, entity.getName());
			try(ResultSet resultSet = selectEntityStatement.executeQuery()) {
				if(resultSet.next()) {
					@SuppressWarnings("unchecked")
					Map<String, String> descMap = (Map<String, String>)resultSet.getObject(2);
					entity.setDescriptions(descMap);
					list.add(entity);
				}
				DatabaseManagement.statisticsRead();
			}catch (SQLException e) {
				throw e;
			}
		}

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
	
	PreparedStatement projectUpsetStatement;
	private final String projectsUpsertSql = "REPLACE INTO PROJECTS(NAME, DESCRIPTIONS) VALUES(?,?)";
	@Override
	public void updateOrInsert(Set<ProjectEntity> entities) throws ActionException {
		try {
			for(ProjectEntity entity:entities) {
				projectUpsetStatement.setString(1, entity.getName());
				projectUpsetStatement.setObject(2, (Object)entity.getDescriptions());
				projectUpsetStatement.execute();
				DatabaseManagement.statisticsWrite();
			}
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}

	private final String projectsDeleteSql = "DELETE FROM PROJECTS";
	@Override
	public void clearDatas() throws ActionException {
		try {
			DatabaseManagement.database.getConnection().createStatement().execute(projectsDeleteSql);
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}
}
