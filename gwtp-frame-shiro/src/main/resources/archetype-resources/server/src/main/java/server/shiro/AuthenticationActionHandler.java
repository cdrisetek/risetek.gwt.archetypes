package ${package}.server.shiro;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.server.auth.IUserManagement;
import ${package}.share.GetNoResult;
import ${package}.share.auth.AuthenticationAction;

public class AuthenticationActionHandler implements ActionHandler<AuthenticationAction, GetNoResult> {

	@Inject
	IUserManagement userManagement;

	@Override
	public GetNoResult execute(AuthenticationAction action, ExecutionContext context) throws ActionException {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = null;

		try {
			if (null == action.principals || action.principals.isEmpty()) {
				// Logout
				currentUser.logout();
				currentUser.getSession().stop();
			} else {
				// Login
				if (currentUser.isAuthenticated())
					throw new ActionException("account had authenticated.");

				if (null == action.credential)
					throw new ActionException("authentication information error.");

				token = new UsernamePasswordToken(action.principals.get(0), action.credential, action.remember);
				currentUser.login(token);

				// 作为OAuth server，需要确认用户的账户所属项目
				// 用户存在，就认为本session有效。存放项目名称到本session。
				SecurityUtils.getSubject().getSession().setAttribute("project", action.project);
			}
		} catch (Throwable t) {
			ActionExceptionMapper.handler(t);
		} finally {
			// For security.
			if (null != token)
				token.clear();
		}

		return new GetNoResult();
	}

	@Override
	public Class<AuthenticationAction> getActionType() {
		return AuthenticationAction.class;
	}

	@Override
	public void undo(AuthenticationAction action, GetNoResult result, ExecutionContext context) throws ActionException {}
}
