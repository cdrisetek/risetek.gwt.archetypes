package ${package}.server.accounts;

import java.util.Set;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.roles.RoleAction;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetResult;
import ${package}.share.exception.ActionUnauthorizedException;

/**
 * @author wangyc@risetek.com
 *
 */
public class RoleActionHandler implements ActionHandler<RoleAction, GetResult<RoleEntity>> {
	@Inject
	IRoleManagement roleManagement;
	
	@Override
	public GetResult<RoleEntity> execute(RoleAction action, ExecutionContext context) throws ActionException {
		Subject subject = SecurityUtils.getSubject();

		if(null != action.roleEntity) {
			if(!subject.isPermitted("roles:update"))
				throw new ActionUnauthorizedException("permission: update roles");
			
			roleManagement.setRoleSet(action.principal, action.project, action.roleEntity.getRoleSet());
			return new GetResult<>(null);
		}

		// Get roles associated
		if(!subject.isPermitted("roles:read"))
			throw new ActionUnauthorizedException("permission: read roles");
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
