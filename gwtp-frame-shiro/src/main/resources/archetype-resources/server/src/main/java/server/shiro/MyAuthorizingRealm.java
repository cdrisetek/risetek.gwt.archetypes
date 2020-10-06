package ${package}.server.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.inject.Inject;
import ${package}.server.auth.IUserManagement;
import ${package}.share.UniqueID;
import ${package}.share.auth.EnumRBAC;

public class MyAuthorizingRealm extends AuthorizingRealm {

	@Inject
	private IUserManagement userManagement;
	
	public MyAuthorizingRealm() {
		setCacheManager(new MemoryConstrainedCacheManager());
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		try {
			// User Identity as authentication Principal.
			UniqueID id = userManagement.authenticate((String)token.getPrincipal(), (char[])token.getCredentials());
			return new SimpleAuthenticationInfo(id, token.getCredentials(), getName());
		} catch (Exception e) {
			throw new AuthenticationException("authenticate failed");
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		if (pc == null)
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		UniqueID id = (UniqueID)pc.getPrimaryPrincipal();
		// Authorization from Local
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//		authorizationInfo.addStringPermission("realm:list");

		// 权限有本地session权限和远程session权限。
		// 如果是 Remote模式，不存在UserManagement，授权应该在AuthorizingRealm中实现。
		// 此处的授权应该仅仅针对Local Session，Remote Session授权不在这里实现。
		// Add local roles.
		// at least it have 'user' role.
		authorizationInfo.addRole(EnumRBAC.USER.name().toLowerCase());
		authorizationInfo.addRoles(userManagement.getUserRoles(id));

		// TODO: Authorization from Remote
		return authorizationInfo;
	}
}
