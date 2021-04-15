package ${package}.server.database.hsqldb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;

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
	
	public HsqldbAccountManagement() {
		DevOpsTask task = new DevOpsTask("Hsqldb AccountsManagement", TaskType.FATAL, null);
		try {
			selectStatement = DatabaseManagement.database.getConnection().prepareStatement(selectSql);
			updateStatement = DatabaseManagement.database.getConnection().prepareStatement(updateSql);
			selectEntityStatement = DatabaseManagement.database.getConnection().prepareStatement(selectEntitySql);
			task.addMessage("table prepare passed");
			task.stat = TaskState.READY;

			new DevOpsTask("Accounts scaner", TaskType.AUTHOR, t -> {
				// Check anybody get DEVELOPER role.
				if(anyMatchedRole("DEVELOPER"))
					t.stat = TaskState.READY;
				else {
					t.stat = TaskState.FAILED;
					t.addMessage("Without basic authorization.");
				}
			});
		} catch (SQLException e) {
			task.addMessage("table prepare failed: " + e.getMessage());
			task.stat = TaskState.FAILED;
		}
	}

	private boolean isLike(AccountEntity entity, String like) {
		if(null == like || entity.getPrincipal().indexOf(like) != -1)
			return true;
		
		Map<String, String> description = entity.getDescriptions();
		
		if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
			return true;

		return false;
	}

	private PreparedStatement selectStatement;
	private final String selectSql = "SELECT NAME, DESCRIPTIONS FROM ACCOUNTS";
	private List<AccountEntity> _readAccounts(String like, int offset, int size) throws SQLException {
		List<AccountEntity> list = new Vector<>();

		int start = 0, count = 0;
		try (ResultSet resultSet = selectStatement.executeQuery()) {
			while(resultSet.next()) {
				AccountEntity entity = new AccountEntity();
				entity.setPrincipal(resultSet.getString(1));
				@SuppressWarnings("unchecked")
				Map<String, String> descMap = (Map<String, String>)resultSet.getObject(2);
				entity.setDescriptions(descMap);

				if(!isLike(entity, like) || start++ < offset)
					continue;
				
				if(count++ >= size)
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
	private final String selectEntitySql = "SELECT NAME, DESCRIPTIONS FROM ACCOUNTS WHERE NAME=?";
	private List<AccountEntity> _readAccountsE(Set<AccountEntity> entities) throws SQLException {
		List<AccountEntity> list = new Vector<>();

		for(AccountEntity entity:entities) {
			selectEntityStatement.setString(1, entity.getPrincipal());
			try (ResultSet resultSet = selectEntityStatement.executeQuery()) {
				if(resultSet.next()) {
					@SuppressWarnings("unchecked")
					Map<String, String> descMap = (Map<String, String>)resultSet.getObject(2);
					System.out.println("readAccounts: " + descMap);
					entity.setDescriptions(descMap);
					list.add(entity);
				}
				DatabaseManagement.statisticsRead();
			} catch (SQLException e) {
				throw e;
			}
		}

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
	/*
	private final String deleteSql = "DELETE FROM ACCOUNTS";
	@Override
	public void clearDatas() throws ActionException {
		try {
			DatabaseManagement.getConnection().createStatement().execute(deleteSql);
		} catch (SQLException e) {
			DatabaseManagement.databaseError(e);
			throw new ActionException("hsqldb:" + e.getMessage());
		}
	}
*/
	
	PreparedStatement updateStatement;
	private final String updateSql = "UPDATE ACCOUNTS SET DESCRIPTIONS=? WHERE NAME=?";
	@Override
	public void updateAccounts(Set<AccountEntity> subjects) throws ActionException {
		try {
			for(AccountEntity entity:subjects) {
				updateStatement.setString(2, entity.getPrincipal());
				updateStatement.setObject(1, entity.getDescriptions());
				updateStatement.execute();
				DatabaseManagement.statisticsWrite();
			}
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}

	@Inject private IAuthorizingHandler authorizing;
	private final String createAccountSql = "INSERT INTO ACCOUNTS(NAME, PASSWORD, DESCRIPTIONS) VALUES(?,?,?)";
	@Override
	public void createAccount(AccountEntity account, String password) throws ActionException {
		try(PreparedStatement stmt = DatabaseManagement.database.getConnection().prepareStatement(createAccountSql)) {
			stmt.setString(1, account.getPrincipal());
			stmt.setObject(2, authorizing.encryptRealmPassword(password));
			stmt.setObject(3, account.getDescriptions());
			stmt.execute();
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}

	private final String getCredentialSql = "SELECT PASSWORD FROM ACCOUNTS WHERE NAME=?";
	@Override
	public Object getCredential(Object principal) throws ActionException {
		try(PreparedStatement stmt = DatabaseManagement.database.getConnection().prepareStatement(getCredentialSql)) {
			stmt.setString(1, (String)principal);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
				return resultSet.getObject(1);
		} catch (SQLException e) {
			throw new ActionException(e);
		}
		return null;
	}

	private final String setCredentialSql = "UPDATE ACCOUNTS SET PASSWORD=? WHERE NAME=?";
	@Override
	public void setCredential(Object principal, Object credential) throws ActionException {
		try(PreparedStatement stmt = DatabaseManagement.database.getConnection().prepareStatement(setCredentialSql)) {
			stmt.setObject(1, authorizing.encryptRealmPassword((String)credential));
			stmt.setString(2, (String)principal);
			stmt.execute();
		} catch (SQLException e) {
			throw new ActionException(e);
		}
	}

	private final String getAccountSql = "SELECT DESCRIPTIONS FROM ACCOUNTS WHERE NAME=?";
	@SuppressWarnings("unchecked")
	@Override
	public AccountEntity getAccount(Object principal) throws ActionException {
		try(PreparedStatement stmt = DatabaseManagement.database.getConnection().prepareStatement(getAccountSql)) {
			stmt.setString(1, (String)principal);
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next()) {
				AccountEntity entity = new AccountEntity();
				entity.setPrincipal((String)principal);
				Map<String, String> descMap = (Map<String, String>)resultSet.getObject(1);
				entity.setDescriptions(descMap);
				return entity;
			}
		} catch (SQLException e) {
			throw new ActionException(e);
		}
		return null;
	}

	@Inject private IRoleManagement roleManagement;
	private final String anyMatchedRoleSql = "SELECT NAME FROM ACCOUNTS";
	@Override
	public boolean anyMatchedRole(String role) {
		try(PreparedStatement stmt = DatabaseManagement.database.getConnection().prepareStatement(anyMatchedRoleSql)) {
			ResultSet resultSet = selectStatement.executeQuery();
			while(resultSet.next()) {
				Object principal = resultSet.getString(1);
				if(roleManagement.getRoleSet(principal, null).stream().anyMatch(r -> r.equals(role)))
					return true;
			}
			DatabaseManagement.statisticsRead();
		} catch (SQLException e) {
			return false;
		} catch (ActionException e) {
			return false;
		}
		return false;
	}
}
