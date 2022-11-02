package ${package}.server.shiro;

import java.util.Map;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTReader;

/**
 * JSON Wrap JWT
 * may deliver Exception information
 * by wangyuchun
 */
public class OAuthJWTAccessTokenResponse extends OAuthAccessTokenResponse {

	public OAuthJWTAccessTokenResponse() {
		super();
	}

	@Override
	public String getAccessToken() {
		return getParam(OAuth.OAUTH_ACCESS_TOKEN);
	}

	@Override
	public String getRefreshToken() {
		return getParam(OAuth.OAUTH_REFRESH_TOKEN);
	}

	@Override
	public String getTokenType() {
		return getParam(OAuth.OAUTH_TOKEN_TYPE);
	}

	@Override
	public Long getExpiresIn() {
		String value = getParam(OAuth.OAUTH_EXPIRES_IN);
		return value == null ? null : Long.valueOf(value);
	}

	@Override
	public String getScope() {
		return getParam(OAuth.OAUTH_SCOPE);
	}

	@Override
	protected void setBody(String body) throws OAuthProblemException {
		try {
			this.body = body;
			Map<String, Object> json = JSONUtils.parseJSON(body);
			error = (String) json.get("error");
			if (null != error) {
				// make TokenValidator happy
				parameters.put(OAuth.OAUTH_ACCESS_TOKEN, "invalid");
				return;
			}

			String jwts = (String) json.get("JWT");
			JWTReader reader = new JWTReader();
			JWT jwt = reader.read(jwts);
			jwt.getClaimsSet().getCustomFields().forEach(action -> {
				parameters.put(action.getKey(), action.getValue());
			});
		} catch (Throwable e) {
			throw OAuthProblemException.error(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE,
					"Invalid response! Response body is not " + OAuth.ContentType.JSON + " encoded");
		}
	}

	@Override
	protected void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	protected void setResponseCode(int code) {
		this.responseCode = code;
	}

	@Override
	public OAuthToken getOAuthToken() {
		return null;
	}

	private String error = null;

	public String getError() {
		return error;
	}
}
