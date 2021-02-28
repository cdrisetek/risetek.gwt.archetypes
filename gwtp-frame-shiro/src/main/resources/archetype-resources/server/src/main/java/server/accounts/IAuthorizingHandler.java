package ${package}.server.accounts;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.accounts.AuthorizationAction;
import ${package}.share.accounts.AuthorizationEntity;
import ${package}.share.dispatch.GetNoResult;
import ${package}.share.dispatch.GetResult;

public interface IAuthorizingHandler {
	GetNoResult doAuthenticationAction(AuthenticationAction action) throws ActionException;
	GetResult<AuthorizationEntity> doAuthorizationAction(AuthorizationAction action) throws ActionException;
	String encryptRealmPassword(String password);
}
