package ${package}.server.shiro;

import java.util.Set;

import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.server.accounts.IAuthorizingHandler;
import ${package}.server.accounts.ISubjectManagement;
import ${package}.server.accounts.roles.IRoleManagement;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.accounts.AuthorizationAction;
import ${package}.share.accounts.AuthorizationEntity;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetNoResult;
import ${package}.share.dispatch.GetResult;

@Singleton
public class MyAuthorizingRealm extends AuthorizingRealm implements IAuthorizingHandler {

	@Inject
	private ISubjectManagement subjectManagement;
	
	@Inject
	private IRoleManagement roleManagement;

	private final PasswordMatcher credentialsMatcher = new PasswordMatcher();
	
	public MyAuthorizingRealm() {
		super();
		setCacheManager(new MemoryConstrainedCacheManager());
		setCredentialsMatcher(credentialsMatcher);
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		try {
			Object password = subjectManagement.getCredential(token.getPrincipal());
			return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
		} catch(Exception e) {
			throw new AuthenticationException("authenticate failed");
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		if (pc == null || null == pc.getPrimaryPrincipal())
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		Object project = subject.getSession().getAttribute("project");
		authorizationInfo.setRoles(roleManagement.getRoleSet(pc.getPrimaryPrincipal(), project));
//		authorizationInfo.addStringPermission("realm:list");
		return authorizationInfo;
	}

	@Override
	public String encryptRealmPassword(String password) {
		return credentialsMatcher.getPasswordService().encryptPassword(password);
	}

	@Override
	public GetNoResult doAuthenticationAction(AuthenticationAction action) throws ActionException {
		GetNoResult result = new GetNoResult();
		Subject subject = SecurityUtils.getSubject();
		if (null == action.principals || action.principals.isEmpty()) {
			// Logout
			subject.logout();
			subject.getSession().stop();
			return result;
		}

		UsernamePasswordToken token = null;
		// Login
		if (null == action.credential)
			throw new ActionException("authentication information error.");

		if (subject.isAuthenticated()) {
		//	throw new ActionException("account had authenticated.");
			// force Logout and try Login
			subject.logout();
			subject.getSession().stop();
		}
		
		token = new UsernamePasswordToken(action.principals.get(0), action.credential, action.remember);
		try {
			subject.login(token);

			// 作为OAuth server，需要确认用户的账户所属项目
			// 用户存在，就认为本session有效。存放项目名称到本session。
			subject.getSession().setAttribute("project", action.project);
		} catch (Throwable t) {
			ActionExceptionMapper.handler(t);
		} finally {
			// For security.
			if (null != token)
				token.clear();
		}

		return new GetNoResult();
	}

	@Override
	public GetResult<AuthorizationEntity> doAuthorizationAction(AuthorizationAction action) throws ActionException {
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		// TODO: authorization just for host project, so we need project name or just a null?
		// TODO: jwt token generator here?
		Set<String> roles = roleManagement.getRoleSet(principal, null);
		AuthorizationEntity authorize = new AuthorizationEntity();
		authorize.setRole(new RoleEntity().addRoleSet(roles));
		return new GetResult<>(authorize);
	}
}
