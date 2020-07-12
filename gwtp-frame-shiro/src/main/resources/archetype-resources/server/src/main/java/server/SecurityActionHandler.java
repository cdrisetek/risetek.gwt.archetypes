package ${package}.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.shiro.UserManagement;
import ${package}.server.shiro.UserManagement.UserInformation;
import ${package}.share.GetResult;
import ${package}.share.RbacConstant;
import ${package}.share.SecurityAction;
import ${package}.share.SecurityInfo;
import ${package}.share.exception.ActionAuthenticationException;

public class SecurityActionHandler implements ActionHandler<SecurityAction, GetResult<SecurityInfo>> {
	private static final Logger logger = Logger.getLogger(SecurityActionHandler.class.getName());

	@Inject
	UserManagement userManagement;
	
	@Override
	public GetResult<SecurityInfo> execute(SecurityAction action, ExecutionContext context) throws ActionException {
		SecurityInfo securityInfo = action.securityInfo;
		if(null == securityInfo)
			throw new ActionException("security action missed informations.");
		
		Map<String, String> attribute = action.securityInfo.getAttributes();
		if(null == attribute)
			throw new ActionException("security action missed attribute.");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(null == currentUser)
			throw new ActionException("security action missed subject.");

		switch(action.op) {
			case LOGIN: {
				logger.fine("login");
				String username = attribute.get("username");
				String password = attribute.get("password");
				String rememberme = attribute.get("rememberme");
				if(null == username || null == password)
					throw new ActionException("security login missed token.");

				// Login
				UsernamePasswordToken upt = new UsernamePasswordToken(
						username, password.toCharArray(),
						"true".equals(rememberme));
				try {
					currentUser.login(upt);
				} catch (Throwable t) {
					logger.fine(username + ": login failed");
					ActionExceptionMapper.handler(t);
				} finally {
					logger.fine(username + ": login successed");
					// For security.
					upt.clear();
				}
				break;
			}

			case LOGOUT:
				// Logout
				currentUser.logout();
				currentUser.getSession().stop();
				break;

			case SYNC:
				break;

			case NEWACCOUNT: {
				String username = attribute.get("username");
				String password = attribute.get("password");
				if(null == username || null == password)
					throw new ActionException("invalid informations.");
				
				break;
			}

			case UPDATE:
				if(!currentUser.isAuthenticated()) {
					logger.fine("failed on update security when unauthenticated");
					throw new ActionException("authenticate failed");
				}
				
				Object principal = currentUser.getPrincipal();
				if(null == principal || !(principal instanceof String))
					throw new ActionException("no principal");

				userManagement.updateSecurity((String)principal, attribute);
				break;

			default:
				break;
				
		}
		
		SecurityInfo info = new SecurityInfo();
		info.setLogin(currentUser.isAuthenticated());
		if(currentUser.isAuthenticated()) {
			info.setPrincipal((String)currentUser.getPrincipal());
			Set<String> roles = RbacConstant.roles.stream().filter(role -> currentUser.hasRole(role))
					.collect(Collectors.toSet());
			info.setRoles(roles);
	
			UserInformation userInfo = userManagement.getUserInfomation((String)currentUser.getPrincipal());
			Map<String, String> attri = new HashMap<>();
				
			if(null != userInfo.email)
				attri.put("email", userInfo.email);
				
			info.setAttributes(attri);
		}
		return new GetResult<>(info);
	}

	@Override
	public Class<SecurityAction> getActionType() {
		return SecurityAction.class;
	}

	@Override
	public void undo(SecurityAction action, GetResult<SecurityInfo> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
