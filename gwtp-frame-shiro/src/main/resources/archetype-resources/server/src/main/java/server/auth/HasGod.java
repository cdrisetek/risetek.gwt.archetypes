package ${package}.server.auth;

import java.util.Set;

import ${package}.share.auth.AccountEntity;

/**
 * User management do not allow update user password and descriptions, but God can.
 * @author wangyc@risetek.com
 *
 */
public interface HasGod {
	// Only update descriptions, not password and state.
	public void updateUsers(Set<AccountEntity> subjects) throws Exception;
	
	/*
	 * 用户只能更新自身的账户信息
	 */
	public void setUserPassword(AccountEntity user, String newpassword);
	public void createUser(AccountEntity user, String password) throws Exception;
}
