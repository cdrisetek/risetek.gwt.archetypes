package ${package}.server.accounts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTWriter;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.shiro.IOAuthConfig;
import ${package}.share.accounts.AccountEntity;

@Singleton
public class AccessTokenManagement {
	@Inject private IAccountManagement accountManagement;
	@Inject private IAuthorizingHandler authorizing;
	@Inject @Nullable private TemporaryAccount temporaryAccount;
	@Inject private IRoleManagement roleManagement;

	public String buildAccessTokenResource(String code, boolean isInternal /* is for internal authentication? */)
			throws OAuthProblemException, OAuthSystemException {
		OAuthInfo info = codeMap.get(code);
		if(null == info)
			throw OAuthProblemException.error("invalide access code");

		String principal = info.getPrincipal();
		Object password = info.getCredential();
		BasicOAuthToken token = info.getToken();

		AccountEntity account = null;
		Set<String> roles = null;

		if(isInternal && null != temporaryAccount) {
			// If have temporaryAccount, use it.
			account = temporaryAccount.getAccountEntity(principal, password);
			if(null != account)
				roles = temporaryAccount.getRoleSet(principal).stream().map(r -> r.name()).collect(Collectors.toSet());
		}
		
		if(null == account) {
			try {
				// Validate
				String credential = (String)accountManagement.getCredential(principal);
				if(null == credential)
					throw OAuthProblemException.error("invalide account");

				if(! authorizing.passwordsMatch(password, credential))
					throw OAuthProblemException.error("invalide password");

				if(null == info.getClientID()) {
					roles = roleManagement.getRoleSet(principal)
				                          .stream().map(r -> r.name()).collect(Collectors.toSet());
				} else
					roles = roleManagement.getRoleSet(principal, info.getClientID());

				if(null == roles || roles.isEmpty())
					throw OAuthProblemException.error("no roles granded");

				account = accountManagement.getAccount(principal);
			} catch (ActionException | OAuthProblemException e) {
				e.printStackTrace();
			}
		}
	
		long time = new Date().getTime();
		JWT jwt = new JWT.Builder().setHeaderAlgorithm("RS256")
		       .setHeaderType("JWT")
		       .setHeaderContentType("JWT")
		       .setClaimsSetIssuer(IOAuthConfig.issuer)
		       .setClaimsSetJwdId("UUID")
		       .setClaimsSetIssuedAt(time)
		       .setClaimsSetExpirationTime(time + token.getExpiresIn())
		       .setClaimsSetSubject(code)
		       .setClaimsSetCustomField(OAuth.OAUTH_ACCESS_TOKEN, token.getAccessToken())
		       .setClaimsSetCustomField(OAuth.OAUTH_REFRESH_TOKEN, token.getRefreshToken())
		       .setClaimsSetCustomField("principal", principal)
               .setClaimsSetCustomField("Roles", roles.stream().collect(Collectors.joining(",")))
               .setClaimsSetCustomField("Descriptions", null==account?null:account.getDescriptions())
		       .setSignature(IOAuthConfig.jwtSignature).build();

		// Wrap JWT with json
		OAuthResponse oAuthResponse = OAuthResponse.status(HttpServletResponse.SC_OK)
				.setParam("JWT", new JWTWriter().write(jwt)).buildJSONMessage();
		
		return oAuthResponse.getBody();
	}
	
	private static Map<String, OAuthInfo> codeMap = new HashMap<>();

	public static class CodeBuilder {
		OAuthInfo info = new OAuthInfo();
		OAuthIssuerImpl issuer = new OAuthIssuerImpl(new MD5Generator());

		public CodeBuilder setPrincipal(String principal) {
			info.setPrincipal(principal);
			return this;
		}

		public CodeBuilder setCredential(String credential) {
			info.setCredential(credential);
			return this;
		}

		public CodeBuilder setClientID(String clientid) {
			info.setClientID(clientid);
			return this;
		}

		public String build() throws OAuthSystemException {
			String authorizationCode = issuer.authorizationCode();
			BasicOAuthToken token = new BasicOAuthToken(
					issuer.accessToken(),
					TokenType.MAC.name(),
					IOAuthConfig.expiration_time,
					issuer.refreshToken(),
					"SCOPE");

			info.setToken(token);
			codeMap.put(authorizationCode, info);
			
			return authorizationCode;
		}
	}

	private static class OAuthInfo {
		public String getPrincipal() {
			return principal;
		}
		public void setPrincipal(String principal) {
			this.principal = principal;
		}
		public Object getCredential() {
			return credential;
		}
		public void setCredential(Object credential) {
			this.credential = credential;
		}
		public String getClientID() {
			return clientID;
		}
		public void setClientID(String clientID) {
			this.clientID = clientID;
		}
		public BasicOAuthToken getToken() {
			return token;
		}
		public void setToken(BasicOAuthToken token) {
			this.token = token;
		}
		@NotNull
		private String principal;
		@NotNull
		private Object credential;
		private String clientID;
		private BasicOAuthToken token;
	}

}
