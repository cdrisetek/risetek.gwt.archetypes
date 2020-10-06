package ${package}.server.shiro;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.auth.IUserManagement;
import ${package}.share.GetResult;
import ${package}.share.auth.AuthorizationAction;
import ${package}.share.auth.RoleEntity;

public class AuthorizationActionHandler implements ActionHandler<AuthorizationAction, GetResult<RoleEntity>> {

	@Inject
	IUserManagement userManagement;
	
	@Override
	public GetResult<RoleEntity> execute(AuthorizationAction action, ExecutionContext context) throws ActionException {
		try {
			return new GetResult<>(new RoleEntity().addRole(userManagement.getSubjectRoles()));
		} catch (Exception e) {
			// throw new ActionException("failed to get current authorization informations: " + e);
			return new GetResult<>(null);
		}
	}

	@Override
	public Class<AuthorizationAction> getActionType() {
		return AuthorizationAction.class;
	}

	@Override
	public void undo(AuthorizationAction action, GetResult<RoleEntity> result, ExecutionContext context)
			throws ActionException {}
}
