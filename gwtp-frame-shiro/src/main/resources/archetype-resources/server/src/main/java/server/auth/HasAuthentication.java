package ${package}.server.auth;

import ${package}.share.UniqueID;

public interface HasAuthentication {
	// return ID of authenticated subject associated User.
	public UniqueID authenticate(String principal, char[] credentials) throws Exception;
}
