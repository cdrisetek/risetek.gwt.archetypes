package ${package}.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.inject.Inject;

public class MyAuthorizingRealm extends AuthorizingRealm {

	@Inject
	private UserManagement userManagement;
	
	public MyAuthorizingRealm() {
		setCacheManager(new MemoryConstrainedCacheManager());
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		if(!(token instanceof UsernamePasswordToken))
			throw new AuthenticationException("invalid token");

		if(!userManagement.isValid((String)token.getPrincipal(), (char [])token.getCredentials()))
			return null;
		System.out.println((String)token.getPrincipal() + " login ok");
		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		if (pc == null)
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");

		String username = (String)pc.getPrimaryPrincipal();
		System.out.println("getPrimaryPrincipal:" + username);

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(userManagement.getRoles(username));
		return authorizationInfo;		
	}
}
