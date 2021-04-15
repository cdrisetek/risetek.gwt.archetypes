package ${package}.server.database.hsqldb;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.hsqldb.jdbc.JDBCArrayBasic;
import org.hsqldb.types.Type;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;
import ${package}.share.templates.Project;

@Singleton
public class HsqldbRoleManagement implements IRoleManagement {

	public HsqldbRoleManagement() {
		DevOpsTask task = new DevOpsTask("Hsqldb RoleManagement", TaskType.FATAL, null);
		try {
			roleSelectStatement = DatabaseManagement.database.getConnection().prepareStatement(roleSelectSql);
			roleUpsertStatement = DatabaseManagement.database.getConnection().prepareStatement(roleUpsertSql);
			task.addMessage("connection and statements passed");
			task.stat = TaskState.READY;
		} catch (SQLException e) {
			task.addMessage("test SQL failed: " + e.getMessage());
			task.stat = TaskState.FAILED;
		}
	}

	private PreparedStatement roleSelectStatement;
	private final String roleSelectSql = "SELECT (ROLES) FROM ROLES WHERE K=?";
	@Override
	public Set<String> getRoleSet(Object principal, Object project) throws ActionException {
		String key = makeKey(principal, project);
		try {
			roleSelectStatement.setString(1, key);
			ResultSet resultSet = roleSelectStatement.executeQuery();
			if(resultSet.next()) {
				Array roleArray = resultSet.getArray(1);
				return Arrays.asList((Object[])roleArray.getArray()).stream()
						.map(obj -> (String)obj).collect(Collectors.toSet());
			}
			DatabaseManagement.statisticsRead();
		} catch (SQLException e) {
			throw new ActionException(e);
		}

		return new HashSet<String>();
	}

	private PreparedStatement roleUpsertStatement;
	private final String roleUpsertSql = "REPLACE INTO ROLES(K, ROLES) VALUES(?,?)";
	@Override
	public void setRoleSet(Object principal, Object project, Set<String> roles) throws ActionException {
		String key = makeKey(principal, project);
		try {
			roleUpsertStatement.setString(1, key);

			// default types defined in org.hsqldb.types.Type can be used
			JDBCArrayBasic array = new JDBCArrayBasic(roles.toArray(), Type.SQL_VARCHAR_DEFAULT);				
			roleUpsertStatement.setArray(2, array);
			roleUpsertStatement.execute();
			DatabaseManagement.statisticsWrite();
		} catch (Throwable e) {
			throw new ActionException(e);
		}
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
