package ${package}.server.accounts;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.HostProjectAction;
import ${package}.share.accounts.HostProjectEntity;
import ${package}.share.dispatch.GetResult;
import ${package}.share.templates.Project;

public class HostProjectActionHandler implements ActionHandler<HostProjectAction, GetResult<HostProjectEntity>> {
	
	@Override
	public GetResult<HostProjectEntity> execute(HostProjectAction action, ExecutionContext context) throws ActionException {
		HostProjectEntity entity = new HostProjectEntity();
		entity.setDescriptions(descriptions);
		return null;
	}

	@Override
	public Class<HostProjectAction> getActionType() {
		return HostProjectAction.class;
	}

	@Override
	public void undo(HostProjectAction action, GetResult<HostProjectEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}

/**
 * Local Project Simple implements
 */
	String projectName = Project.name;
	String[] descriptions = {
			"this is Local project informations",
			"you should change this informatisons",
			"what else informations?",
			"local project roles setting in LocalEntity as Enums",
	};
}
