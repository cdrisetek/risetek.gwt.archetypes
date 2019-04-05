package com.risetek;

import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.risetek.shiro.UserManagement;

public class UpdateSecurityActionHandler implements ActionHandler<UpdateSecurityAction, GetNoResult> {

	@Inject
	UserManagement userManagment;
	
	@Override
	public GetNoResult execute(UpdateSecurityAction action, ExecutionContext context) throws ActionException {
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isAuthenticated())
			return null;
		Object principal = currentUser.getPrincipal();
		if(null == principal || !(principal instanceof String))
			return null;
		
		userManagment.updateSecurity((String)principal, action.security);
		return null;
	}

	@Override
	public Class<UpdateSecurityAction> getActionType() {
		return UpdateSecurityAction.class;
	}

	@Override
	public void undo(UpdateSecurityAction action, GetNoResult result, ExecutionContext context) throws ActionException {
		// do nothing.
	}

}
