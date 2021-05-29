package ${package}.server.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.as.response.OAuthASResponse.OAuthAuthorizationResponseBuilder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.jwt.request.JWTOAuthRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ${package}.server.accounts.AccessTokenManagement;

/**
 * OAuth Server Handler
 * 
 * PATH /oauth/authz OAuth authentication and/or authorization process
 * authentication for account with credential, store OAuthIssuer for OAuth
 * client. and then redirect to call back URL.
 * 
 * @author wangyc@risetek.com
 *
 */

@Singleton
public class OAuthAuthzServlet extends HttpServlet {

	private static final long serialVersionUID = -1969643036046255438L;
	@Inject private AccessTokenManagement accessTokenManagement;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		try {
			try {
				// dynamically recognize an OAuth profile based on request
				// characteristic (params, method, content type etc.), perform validation
				OAuthAuthzRequest oRequest = new OAuthAuthzRequest(request);
				ResponseType type = ResponseType.valueOf(oRequest.getResponseType().toUpperCase());

				switch(type) {
				case CODE: {
					String principal = oRequest.getParam(OAuth.OAUTH_USERNAME);
					String credential = oRequest.getParam(OAuth.OAUTH_PASSWORD);
					if(null == principal || null == credential)
						throw OAuthProblemException.error("requires username and password");

					AccessTokenManagement.CodeBuilder builder = new AccessTokenManagement.CodeBuilder()
							                        .setPrincipal(principal)
					                                .setCredential(credential);
					if(! IOAuthConfig.client_id_local.equals(oRequest.getClientId()))
						builder.setClientID(oRequest.getClientId());

					String authorizationCode = builder.build();

					// build OAuth response
					OAuthAuthorizationResponseBuilder codeBuilder = OAuthASResponse
							.authorizationResponse(request, HttpServletResponse.SC_FOUND)
							.setCode(authorizationCode)
							.setExpiresIn(0L)
							.location(oRequest.getRedirectURI());

					pw.print(codeBuilder.buildJSONMessage().getBody());
					break;
				}
				case TOKEN: {
					// Validates whether a given HttpServletRequest is a valid OAuth Token request.
					JWTOAuthRequest oauthRequest = new JWTOAuthRequest(request);
					String code = oauthRequest.getCode();
					if(null == code)
						throw OAuthProblemException.error("missing access code");

					pw.print(accessTokenManagement.buildAccessTokenResource(code, false /* is for internal authentication? */));
					response.setStatus(HttpServletResponse.SC_OK);
					break;
				}
				default:
					System.out.println("Falied on AuthzServlet for: " + type);
					break;
				}

				pw.flush();
				pw.close();
			} catch (OAuthProblemException ex) {
				System.out.println("AuthProblem: " + ex.getMessage());
				// if something goes wrong
				OAuthResponse resp = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_FOUND)
						.setError(ex.getMessage())
						.buildJSONMessage();
				pw.print(resp.getBody());
				pw.flush();
				pw.close();
			}
		} catch (OAuthSystemException e) {
			System.out.println("OAuthSystem " + e.getMessage());
		} catch (Exception e) {
			System.out.println("2--> " + e.getMessage());
		}
	}
}
