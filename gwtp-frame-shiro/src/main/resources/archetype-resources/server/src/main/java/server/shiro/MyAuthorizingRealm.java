package ${package}.server.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthClientResponseFactory;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.AccessTokenManagement;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.IAuthorizingHandler;
import ${package}.server.accounts.ISubjectManagement;
import ${package}.server.shiro.oltu.client.OAuthClientToken;
import ${package}.server.shiro.oltu.client.OAuthConstants;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.accounts.AuthorizationAction;
import ${package}.share.accounts.AuthorizationEntity;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetNoResult;
import ${package}.share.dispatch.GetResult;
import ${package}.share.exception.ActionAuthenticationException;
import ${package}.share.exception.ActionUnauthenticatedException;

@Singleton
public class MyAuthorizingRealm extends IniRealm implements IAuthorizingHandler, ISubjectManagement, IOAuthConfig {
	static Logger logger = Logger.getLogger(MyAuthorizingRealm.class.getName());

	/**
	 * sub-class should override this method to request principal attributes, these
	 * attributes will put as Subject second principal in type Map. Note
	 * {@link OAuth#OAUTH_ACCESS_TOKEN} and {@link OAuth#OAUTH_EXPIRES_IN} and
	 * {@link OAuth#OAUTH_REFRESH_TOKEN} and {@link OAuthConstants#OAUTH_TOKEN_TIME}
	 * and {@link OAuthConstants#OAUTH_SCOPES} will be put later, so do not use
	 * those five keys.
	 * 
	 * @param oAuthResponse OAuth access token response
	 * @return principal attributes, if the returned attributes contains key
	 *         <b>principal</b>, then the value will be used as Subject primary
	 *         principal
	 */
	protected Map<String, Object> requestAttributes(OAuthAccessTokenResponse oAuthResponse) {
		return null;
	}

	/**
	 * create authentication info, by default, this create SimpleAuthenticationInfo
	 * with principals using access token as primary principal and a map contains
	 * attributes {@link OAuth#OAUTH_ACCESS_TOKEN} and
	 * {@link OAuth#OAUTH_EXPIRES_IN} and {@link OAuth#OAUTH_REFRESH_TOKEN} and
	 * {@link OAuthConstants#OAUTH_TOKEN_TIME} and
	 * {@link OAuthConstants#OAUTH_SCOPES}, the credentials set to byte array of
	 * access token. if sub-class override requestAttributes and returned attributes
	 * contains key {@link OAuthConstants#OAUTH_PRINCIPAL}, then the value will be
	 * used as primary principal.
	 * 
	 * @param clientToken   the client token
	 * @param oAuthResponse OAuth access token response
	 * @return authentication info
	 */
	private AuthenticationInfo buildAuthenticationInfo(OAuthClientToken clientToken,
			OAuthAccessTokenResponse oAuthResponse) {
		String accessToken = oAuthResponse.getAccessToken();
		Date tokenTime = new Date();
		Map<String, Object> attributes = requestAttributes(oAuthResponse);
		if (attributes == null)
			attributes = new HashMap<String, Object>();
		else
			attributes = new HashMap<String, Object>(attributes);
		List<Object> principals = new ArrayList<Object>();
		if (attributes.containsKey(OAuthConstants.OAUTH_PRINCIPAL))
			principals.add(attributes.get(OAuthConstants.OAUTH_PRINCIPAL));
		else
			principals.add(accessToken);
		attributes.put(OAuth.OAUTH_ACCESS_TOKEN, accessToken);
		attributes.put(OAuth.OAUTH_EXPIRES_IN, oAuthResponse.getExpiresIn());
		attributes.put(OAuth.OAUTH_REFRESH_TOKEN, oAuthResponse.getRefreshToken());
		attributes.put(OAuthConstants.OAUTH_TOKEN_TIME, tokenTime);
		attributes.put(OAuthConstants.OAUTH_SCOPES, clientToken.getScopes());
		principals.add(attributes);

		principals.add(oAuthResponse.getParam("principal"));

		attributes.put("principal", oAuthResponse.getParam("principal"));
		attributes.put("Roles", oAuthResponse.getParam("Roles"));
		attributes.put("Descriptions", oAuthResponse.getParam("Descriptions"));
		
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setPrincipal(oAuthResponse.getParam("principal"));
		
		Map<String, Object> descriptions = JSONUtils.parseJSON(oAuthResponse.getParam("Descriptions"));
		Map<String, String> sdescriptions = descriptions.entrySet().stream()
				                           .collect(Collectors.toMap( e -> e.getKey(), e -> (String)e.getValue()));
		
		principals.add(accountEntity);
		accountEntity.setDescriptions(sdescriptions);

		PrincipalCollection collection = new SimplePrincipalCollection(principals, getName());
		return new SimpleAuthenticationInfo(collection, accessToken);
	}

	private final PasswordMatcher credentialsMatcher = new PasswordMatcher();
	@Override
	public String encryptRealmPassword(Object password) {
		if(null == password)
			return null;
		return credentialsMatcher.getPasswordService().encryptPassword(password);
	}
	
	@Override
	public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
		return credentialsMatcher.getPasswordService().passwordsMatch(submittedPlaintext, encrypted);
	}
	
	public MyAuthorizingRealm() {
		super();
		setCacheManager(new MemoryConstrainedCacheManager());
	    setAuthenticationTokenClass(OAuthClientToken.class);
	    setCredentialsMatcher(new AllowAllCredentialsMatcher());
	}
	
	// TODO: how to decouple from accounts?
	@Inject private AccessTokenManagement accessTokenManagement;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
	    OAuthClientToken clientToken = (OAuthClientToken) token;
	    
	    // TODO: if configure endpointLocation to null, exchange JWTToken internal
	    
	    String location;
	    if(null == IOAuthConfig.endpointLocation) {
	    	System.out.println("Internal exchange code/token");
			try {
				OAuthAccessTokenResponse response = OAuthClientResponseFactory.createCustomResponse(
						accessTokenManagement.buildAccessTokenResource(clientToken.getAuthCode(), true /* is for internal authentication? */),
						null, 0, null, OAuthJWTAccessTokenResponse.class);
				
				return buildAuthenticationInfo(clientToken, response);
			} catch (OAuthProblemException | OAuthSystemException e) {
			      throw new AuthenticationException(e.getMessage(), e);
			}
	    } else
	    	location = IOAuthConfig.endpointLocation + IOAuthConfig.authzURI;

		OAuthClient client = new OAuthClient(new URLConnectionClient());
	    try {
	      OAuthClientRequest oAuthRequest = OAuthClientRequest.tokenLocation(location)
	          .setClientId(IOAuthConfig.client_id).setClientSecret(IOAuthConfig.client_secret)
              .setParameter(OAuth.OAUTH_RESPONSE_TYPE, ResponseType.TOKEN.toString())
              .setParameter(OAuth.OAUTH_TOKEN_TYPE, TokenType.MAC.toString())
              .setAssertion("assert")
              .setAssertionType(OAuth.ASSERTION)
              .setGrantType(clientToken.getGrantType())
	          .setCode(clientToken.getAuthCode())
	          .setRefreshToken(clientToken.getRefreshToken()).setRedirectURI("/")
	          .buildBodyMessage();
	      return buildAuthenticationInfo(clientToken, client.accessToken(oAuthRequest, OAuthJWTAccessTokenResponse.class));
	    } catch (OAuthSystemException | OAuthProblemException ex) {
	      throw new AuthenticationException(ex.getMessage(), ex);
	    } finally {
	      client.shutdown();
	    }
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		if (pc == null || null == pc.getPrimaryPrincipal())
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = pc.oneByType(Map.class);
		
		Set<String> myRoles = Arrays.asList(map.get("Roles").split(",")).stream().collect(Collectors.toSet());

		try {
			Set<HostProjectRBAC> roles = myRoles.stream().map(r -> HostProjectRBAC.valueOf(r)).collect(Collectors.toSet());
			// NOTE: convert will lost some role?
			authorizationInfo.setRoles(roles.stream().map(r -> r.name()).collect(Collectors.toSet()));
			Set<String> permissions = roles.stream().flatMap(r -> r.permissions().stream()).collect(Collectors.toSet());
			authorizationInfo.addStringPermissions(permissions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorizationInfo;
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
			throw new ActionAuthenticationException("authentication information error.");

		if (subject.isAuthenticated()) {
		//	throw new ActionException("account had authenticated.");
			// force Logout and try Login
			subject.logout();
			subject.getSession().stop();
		}
		
		token = new UsernamePasswordToken(action.principals.get(0), "password" /* oauth verified */, action.remember);
		try {
			subject.login(token);
			// 作为OAuth server，需要确认用户的账户所属项目
			// 用户存在，就认为本session有效。存放项目名称到本session。
			subject.getSession().setAttribute("project", action.project);
			// TEST
			subject.getSession().setTimeout(20*60*1000);
		} catch (Throwable t) {
			logger.severe(action.principals.get(0) + " login failed: " + t.getMessage());
			t.printStackTrace();
			throw new ActionAuthenticationException("Login failed: " + t.getMessage());
		} finally {
			// For security.
			if (null != token)
				token.clear();
		}

		return result;
	}

	// For GWT client
	@Override
	public GetResult<AuthorizationEntity> doAuthorizationAction(AuthorizationAction action) throws ActionException {
		Subject subject = SecurityUtils.getSubject();
		if(null == subject)
			return null;

		PrincipalCollection pc = subject.getPrincipals();
		if(null == pc)
			return null;
		
		AuthorizationInfo info = getAuthorizationInfo(subject.getPrincipals());
		if(null == info)
			return null;
		
		AuthorizationEntity authorize = new AuthorizationEntity();
		authorize.setRole(new RoleEntity().addRoleSet(info.getRoles().stream().collect(Collectors.toSet())));
		return new GetResult<>(authorize);
	}

	// For GWT client
	@Override
	public AccountEntity getSubjectEntity() throws ActionException {
		Subject subject = SecurityUtils.getSubject();
		if(null == subject)
			return null;
		
		PrincipalCollection pc = subject.getPrincipals();
		if(null == pc)
			return null;
		
		return pc.oneByType(AccountEntity.class);
	}
	
	// TODO: should implement elsewhere?
	@Inject private IAccountManagement accountManagement;
	@Override
	public void setSubjectPassword(String password) throws ActionException {
		if(null == password || password.isEmpty()) {
			System.out.println("resetCurrentUserPassword");
			// TODO: should send email for this.
			password = "randomresetpassword";
		}

		Subject subject = SecurityUtils.getSubject();
		for(Object o:subject.getPrincipals()) {
			if(o instanceof AccountEntity) {
				AccountEntity e = (AccountEntity)o;
				System.out.println("set password for user: " + e.principal);
				accountManagement.setCredential(e.principal, password);
				
				// Logout and force user login with new password again.
				subject.logout();
				throw new ActionUnauthenticatedException("Force logout for password changed.");
			}
		}
		throw new ActionException("Principal not found, it is impossible.");
	}
	// End of ISubjectManagement interface implements
}
