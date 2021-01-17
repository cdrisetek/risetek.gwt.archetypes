package ${package}.server.auth;

import java.util.List;
import java.util.Set;

import ${package}.share.auth.AccountEntity;
import ${package}.share.auth.accounts.AccountStateEntity;

public interface IUserManagement extends HasAuthorization, HasSubject, HasGod, HasAuthentication {
	public List<AccountEntity> readUsers(Set<AccountEntity> entities, final String like, int offset, int size) throws Exception;

	public void createUser(AccountEntity user) throws Exception;
	public void setUserState(AccountEntity user, AccountStateEntity state) throws Exception;
}
