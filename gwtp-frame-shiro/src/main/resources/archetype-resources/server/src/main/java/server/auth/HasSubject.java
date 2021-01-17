package ${package}.server.auth;

import java.util.Set;

import ${package}.share.auth.AccountEntity;

public interface HasSubject {
	/**
	 * @return Current subject associated user entity.
	 * @throws Exception
	 */
	public AccountEntity getSubjectUserEntity() throws Exception;
	public Set<String> getSubjectRoles() throws Exception;

	public void setSubjectUserPassword(String password) throws Exception;
}
