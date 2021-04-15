package ${package}.server.accounts;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthClientResponseFactory;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse.OAuthResponseBuilder;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTWriter;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.shiro.IOAuthConfig;
import ${package}.server.shiro.OAuthJWTAccessTokenResponse;
import ${package}.share.accounts.AccountEntity;

@Singleton
public class AccessTokenManagement {
	@Inject private IAccountManagement accountManagement;
	@Inject private IAuthorizingHandler authorizing;
	@Inject private TemporaryAccount temporaryAccount;
	@Inject private IRoleManagement roleManagement;

	private OAuthResponseBuilder json;

	public String buildAccessTokenResource(String code) throws OAuthProblemException, OAuthSystemException {
		OAuthInfo info = codeMap.get(code);
		if(null == info)
			throw OAuthProblemException.error("invalide access code");

		boolean isDemo = info.isDemo();
		
		try {

		if(!isDemo) {
			// Validate
			String credential = Optional.ofNullable(authorizing.encryptRealmPassword(temporaryAccount.getCredential(info.getPrincipal())))
			                           .orElse((String)accountManagement.getCredential(info.getPrincipal()));
			if(null == credential)
				throw OAuthProblemException.error("invalide account");

			if(! authorizing.passwordsMatch(info.getCredential(), credential))
				throw OAuthProblemException.error("invalide password");
		}

		Set<String> roles;
		if(isDemo)
			roles = Arrays.asList("DEVELOPER", "GUEST").stream().collect(Collectors.toSet());
		else if(null == info.getClientID()) {
			roles = Optional.ofNullable(temporaryAccount.getRoleSet(info.getPrincipal()))
                     .orElse(roleManagement.getRoleSet(info.getPrincipal()))
                     .stream().map(r -> r.name()).collect(Collectors.toSet());
		} else
			roles = roleManagement.getRoleSet(info.getPrincipal(), info.getClientID());

		if(null == roles || roles.isEmpty())
			throw OAuthProblemException.error("no roles granded");

		AccountEntity account = Optional.ofNullable(temporaryAccount.getAccountEntity())
				.orElse(accountManagement.getAccount(info.getPrincipal()));
		
		BasicOAuthToken token = info.getToken();

		long time = new Date().getTime();
		JWT.Builder builder = new JWT.Builder();
		builder.setHeaderAlgorithm("RS256")
		       .setHeaderType("JWT")
		       .setHeaderContentType("JWT")
		       .setClaimsSetIssuer(IOAuthConfig.issuer)
		       .setClaimsSetJwdId("UUID")
		       .setClaimsSetIssuedAt(time)
		       .setClaimsSetExpirationTime(time + token.getExpiresIn())
		       .setClaimsSetSubject(code)
		       .setClaimsSetCustomField(OAuth.OAUTH_ACCESS_TOKEN, token.getAccessToken())
		       .setClaimsSetCustomField(OAuth.OAUTH_REFRESH_TOKEN, token.getRefreshToken())
		       .setClaimsSetCustomField("principal", info.getPrincipal())
               .setClaimsSetCustomField("Roles", roles.stream().collect(Collectors.joining(",")))
               .setClaimsSetCustomField("Descriptions", null==account?null:account.getDescriptions())
		       .setSignature(IOAuthConfig.jwtSignature);

		if(info.isDemo())
			builder.setClaimsSetCustomField("DEMO", true);

		// Wrap JWT with json
		json = OAuthResponse.status(HttpServletResponse.SC_OK)
				.setParam("JWT", new JWTWriter().write(builder.build()));
		} catch(ActionException e) {
			throw OAuthProblemException.error(e.getMessage());
		}
		
		return json.buildJSONMessage().getBody();
	}

	// For internal exchange OAuthCode for OAuthToken
	public OAuthAccessTokenResponse getOAuthAccessTokenResponseInternal(String accessCode) {
		OAuthAccessTokenResponse response = null;
		try {
			response = OAuthClientResponseFactory
			.createCustomResponse(buildAccessTokenResource(accessCode), null, 0, null, OAuthJWTAccessTokenResponse.class);
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
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

		public CodeBuilder setDemo(boolean isDemo) {
			info.setDemo(isDemo);
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
		public boolean isDemo() {
			return isDemo;
		}
		public void setDemo(boolean isDemo) {
			this.isDemo = isDemo;
		}
		@NotNull
		private String principal;
		@NotNull
		private Object credential;
		private String clientID;
		private BasicOAuthToken token;
		private boolean isDemo = true;
	}

}
