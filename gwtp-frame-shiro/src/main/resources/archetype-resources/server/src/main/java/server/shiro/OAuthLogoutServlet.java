package ${package}.server.shiro;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * OAuth Client
 *  
 * @author wangyc@risetek.com
 *
 */

@Singleton
public class OAuthLogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 6606746574998787864L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Subject subject = SecurityUtils.getSubject();
		// Logout
		subject.logout();
		subject.getSession().stop();
		resp.sendRedirect("/");
	}
}
