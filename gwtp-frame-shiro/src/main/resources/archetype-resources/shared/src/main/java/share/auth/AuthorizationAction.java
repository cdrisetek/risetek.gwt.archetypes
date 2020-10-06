package ${package}.share.auth;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.GetResult;

/**
 * AuthorizationAction require server side check subject and return roles it authorized.
 * 
 * @author wangyc@risetek.com
 *
 */
public class AuthorizationAction extends UnsecuredActionImpl<GetResult<RoleEntity>> {
	public AuthorizationAction() {
	}
}
