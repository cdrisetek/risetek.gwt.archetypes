package ${package}.server.accounts.roles;

import java.util.Set;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.roles.RoleAction;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetResult;

/**
 * @author wangyc@risetek.com
 *
 */
public class RoleActionHandler implements ActionHandler<RoleAction, GetResult<RoleEntity>> {
	@Inject
	IRoleManagement roleManagement;
	
	@Override
	public GetResult<RoleEntity> execute(RoleAction action, ExecutionContext context) throws ActionException {
		if(null != action.roleEntity) {
			roleManagement.setRoleSet(action.principal, action.project, action.roleEntity.getRoleSet());
			return new GetResult<>(null);
		}

		// Get roles associated
		Set<String> roles = roleManagement.getRoleSet(action.principal, action.project);
		return new GetResult<>(new RoleEntity().addRoleSet(roles));
	}

	@Override
	public Class<RoleAction> getActionType() {
		return RoleAction.class;
	}

	@Override
	public void undo(RoleAction action, GetResult<RoleEntity> result, ExecutionContext context) throws ActionException {}
}
