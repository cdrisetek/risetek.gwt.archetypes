package ${package}.server;

import javax.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.shiro.UserManagement;
import ${package}.server.shiro.UserManagement.AccountRecord;
import ${package}.share.GetResult;
import ${package}.share.SecurityAction;
import ${package}.share.realmgt.AccountEntity;

public class SecurityActionHandler implements ActionHandler<SecurityAction, GetResult<AccountEntity>> {

	@Inject
	UserManagement userManagement;
	
	@Override
	public GetResult<AccountEntity> execute(SecurityAction action, ExecutionContext context) throws ActionException {
		Subject currentUser = SecurityUtils.getSubject();

		try {
		switch(action.op) {
			case LOGIN: {
				if(currentUser.isAuthenticated())
					throw new ActionException("account had authenticated.");

				String username = action.subject.getAccountPrincipal();
				String password = action.password;
				if(null == username || null == password)
					throw new ActionException("security login missed token.");

				// Login
				UsernamePasswordToken token = new UsernamePasswordToken(
						username, password.toCharArray(), action.rememberme);
				try {
					currentUser.login(token);
				} catch (Throwable t) {
					ActionExceptionMapper.handler(t);
				} finally {
					// For security.
					token.clear();
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
				/*
				String username = attribute.get("username");
				String password = attribute.get("password");
				if(null == username || null == password)
					throw new ActionException("invalid informations.");
				*/
				
				break;
			}

			case UPDATE:
				String principal = (String)currentUser.getPrincipal();
				userManagement.updateSecurity(principal, action.password, action.subject);
				if(null != action.password) {
					// Logout, force user login with new password.
					currentUser.logout();
					currentUser.getSession().stop();
				}
				break;

			default:
				break;
				
		}
		} catch(Throwable t) {
			ActionExceptionMapper.handler(t);
		}
		
		if(currentUser.isAuthenticated()) {
			AccountRecord account = userManagement.getUserInfomation((String)currentUser.getPrincipal());
			return new GetResult<>(account.subject);
		}
		return new GetResult<>(null);
	}

	@Override
	public Class<SecurityAction> getActionType() {
		return SecurityAction.class;
	}

	@Override
	public void undo(SecurityAction action, GetResult<AccountEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
