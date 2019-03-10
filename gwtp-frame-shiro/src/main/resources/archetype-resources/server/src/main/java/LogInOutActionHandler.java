package ${package};

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.risetek.shiro.UserManagement;

public class LogInOutActionHandler implements
		ActionHandler<LoginOutAction, GetResult<AuthorityInfo>> {

	private static final Logger logger = Logger.getLogger(LogInOutActionHandler.class.getName());
	@Inject UserManagement userManagement;
	
/**
 * 1. null == token for logout
 * 2. null == username for get authority information, used on client bootup
 * 3. full token information for login, and return authority information.
 */
	@Override
	public GetResult<AuthorityInfo> execute(LoginOutAction action, ExecutionContext context) throws ActionException {
		AuthorityInfo info = new AuthorityInfo();
		AuthToken token = action.authToken;
		Subject currentUser = SecurityUtils.getSubject();
		
		if(null == token) {
			// Logout
			SecurityUtils.getSubject().logout();
			SecurityUtils.getSubject().getSession().stop();
		} else if(!currentUser.isAuthenticated() && (null != token.getUsername())) {
			// Login
			UsernamePasswordToken upt = new UsernamePasswordToken(
					token.getUsername(), token.getPassword().toCharArray(),
					token.isRememberMe());
			try {
				currentUser.login(upt);
			} catch (Exception e) {
				logger.fine(token.getUsername() + ": login failed");
				throw new ActionException(e.getMessage(), e);
			} finally {
				logger.fine(token.getUsername() + ": login successed");
				// For security.
				upt.clear();
			}
		}

		info.setLogin(currentUser.isAuthenticated());
		info.setUsername((String)currentUser.getPrincipal());
		Set<String> roles = userManagement.allRoles.stream().filter(role -> currentUser.hasRole(role))
				.collect(Collectors.toCollection(HashSet::new));
		info.setRoles(roles);
	
		return new GetResult<>(info);
	}

	@Override
	public void undo(LoginOutAction action, GetResult<AuthorityInfo> result, ExecutionContext context)
			throws ActionException {
		// do nothing
	}

	@Override
	public Class<LoginOutAction> getActionType() {
		return LoginOutAction.class;
	}
}
