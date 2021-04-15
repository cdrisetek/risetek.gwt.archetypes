package ${package}.server.accounts;

import java.util.List;
import java.util.Set;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.AccountEntity;

public interface IAccountManagement {
	List<AccountEntity> readAccounts(Set<AccountEntity> entities, final String like, int offset, int size) throws ActionException;
	// Only update descriptions, not password.
	void updateAccounts(Set<AccountEntity> subjects) throws ActionException;
	
	void createAccount(AccountEntity account, String password) throws ActionException;
	
	Object getCredential(Object principal) throws ActionException;
	void setCredential(Object principal, Object credential) throws ActionException;
	AccountEntity getAccount(Object principal) throws ActionException;
	boolean anyMatchedRole(String role);
}
