package ${package}.login;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import ${package}.shiro.UserManagement;

/**
 * The server side implementation of the GreetingContext.
 */
public class LoginService {
	private static final Logger logger = Logger.getLogger(LoginService.class.getName());

	private final UserManagement userManagement;

	@Inject
	LoginService(UserManagement userManagement) {
		this.userManagement = userManagement;
	}
	
	public AuthorityResponse login(String username, String passwd) {
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isAuthenticated() && (null != username)) {
			UsernamePasswordToken upToken = 
					new UsernamePasswordToken(username, passwd.toCharArray());
			upToken.setRememberMe(true);
	
			try {
				currentUser.login(upToken);
			} catch (Exception e) {
				logger.severe(username + " login failed.");
				// throw this exception to server, so client will receive a ServerFailure response.
				throw e;
			} finally {
				// For security.
				upToken.clear();
			}
		}
		
		AuthorityResponse response = new AuthorityResponse();
		response.setLogin(currentUser.isAuthenticated());

		Set<String> roles = userManagement.allRoles.stream().filter(role -> currentUser.hasRole(role))
				.collect(Collectors.toCollection(HashSet::new));
		response.setRoles(roles);

		return response;
	}
	
	public void logout() {
		// Logout
		SecurityUtils.getSubject().logout();
		SecurityUtils.getSubject().getSession().stop();
	}
}
