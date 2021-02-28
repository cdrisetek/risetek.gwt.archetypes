package ${package}.server.accounts;

import ${package}.share.accounts.AccountEntity;

public interface ISubjectManagement {
	/**
	 * @return Current subject associated user entity.
	 * @throws Exception
	 */
	AccountEntity getSubjectEntity() throws Exception;
	Object getCredential(Object principal) throws Exception;
	void setSubjectPassword(String password) throws Exception;
}
