package ${package}.server.shiro;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

import com.google.inject.Singleton;
import ${package}.server.shiro.oltu.client.OAuthClientToken;

/**
 * OAuth Client Handler
 * 
 * PATH /oauth/token
 * OAuth Server redirect your Browser to here when get access code.
 * OAuth Client request OAuth Server token with access code.
 * @author wangyc@risetek.com
 *
 */

@Singleton
public class OAuthTokenServlet extends HttpServlet implements IOAuthConfig {
	private static final long serialVersionUID = -6683598824382339693L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// request from Browser which should from OAuth Client.
		// request include 'code', 'client_secret' parameter.
		// response return access token and refresh token, which should for OAuth Server.
		try {
			OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			// You should first compare this state value to ensure it matches the one you
			// started with. You can typically store the state value in a cookie or session,
			// and compare it when the user comes back.
			String state = oar.getState();
			if(state == null || !state.equals(request.getSession().getAttribute(IOAuthConfig.state_key)))
				throw OAuthProblemException.error("OAuth state mistaken");
			
			String code = oar.getCode();
			if(null == code)
				throw OAuthProblemException.error("OAuth code mistaken");

			// TODO: create OAuthClientToken and login.
			OAuthClientToken token = OAuthClientToken.authCode(code, Arrays.asList("scope1", "scope2").stream().collect(Collectors.toSet()));
			Subject subject = SecurityUtils.getSubject();
			if(null != request.getParameter(IOAuthConfig.rememberme_key))
				token.setRememberMe(true);

			try {
				subject.login(token);
				// 作为OAuth server，需要确认用户的账户所属项目
				// 用户存在，就认为本session有效。存放项目名称到本session。
				//subject.getSession().setAttribute("project", action.project);
				// TEST
				//subject.getSession().setTimeout(20*60*1000);
				
				// TODO: here we successed!
				response.sendRedirect("/");
				
			} catch (AuthenticationException e) {
				throw OAuthProblemException.error("Login failed: ", e.getMessage());
			}
		} catch (OAuthProblemException e) {
			// if something goes wrong
			try {
				OAuthResponse resp = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
					.error(e)
					.location("/#/error")
					.buildQueryMessage();
				response.sendRedirect(resp.getLocationUri());
			} catch (OAuthSystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
