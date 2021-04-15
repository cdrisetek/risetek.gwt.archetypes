package ${package}.server.accounts;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.dispatch.GetNoResult;

public class AuthenticationActionHandler implements ActionHandler<AuthenticationAction, GetNoResult> {

	@Inject
	IAuthorizingHandler authorizing;

	@Override
	public GetNoResult execute(AuthenticationAction action, ExecutionContext context) throws ActionException {
		try {
			return authorizing.doAuthenticationAction(action);
		} catch(ActionException e) {
			return null;
		}
	}

	@Override
	public Class<AuthenticationAction> getActionType() {
		return AuthenticationAction.class;
	}

	@Override
	public void undo(AuthenticationAction action, GetNoResult result, ExecutionContext context) throws ActionException {}
}
