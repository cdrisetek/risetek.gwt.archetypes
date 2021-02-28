package ${package}.server.accounts;

import java.util.List;
import java.util.Set;

import ${package}.share.accounts.AccountEntity;

public interface IAccountManagement {
	List<AccountEntity> readAccounts(Set<AccountEntity> entities, final String like, int offset, int size) throws Exception;
	// Only update descriptions, not password.
	void updateAccounts(Set<AccountEntity> subjects) throws Exception;
	
	void createAccount(AccountEntity user, String password) throws Exception;
	void shutdown();
}
