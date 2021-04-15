package ${package}.server.devops;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IProjectsManagement;
import ${package}.share.devops.DevOpsAction;
import ${package}.share.devops.DevOpsAction.OP;
import ${package}.share.dispatch.GetNoResult;

public class DevOpsActionHandler implements ActionHandler<DevOpsAction, GetNoResult> {
	@Inject IProjectsManagement projectsManagement;
	
	@Override
	public GetNoResult execute(DevOpsAction action, ExecutionContext context) throws ActionException {
		if(action.op == OP.CLEAR_PROJECT) {
			System.out.println("DEBUG: DevOpsActionHandler invoke, clear all project datas");
			projectsManagement.clearDatas();
		}
			
		System.out.println("DEBUG: DevOpsActionHandler invoke");
		return null;
	}

	@Override
	public Class<DevOpsAction> getActionType() {
		return DevOpsAction.class;
	}

	@Override
	public void undo(DevOpsAction action, GetNoResult result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
