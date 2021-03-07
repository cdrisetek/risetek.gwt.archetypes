package ${package}.server;

import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.projects.IProjectsManagement;
import ${package}.server.accounts.roles.IRoleManagement;
import ${package}.share.container.StateAction;
import ${package}.share.container.StateEntity;
import ${package}.share.dispatch.GetResults;

public class StateActionHandler implements ActionHandler<StateAction, GetResults<StateEntity>> {
	@Inject
	private IAccountManagement accountManagement;
	@Inject
	private IProjectsManagement projectManagement;
	@Inject
	private IRoleManagement roleManagement;
	
	@Override
	public GetResults<StateEntity> execute(StateAction action,
			ExecutionContext context) throws ActionException {
		List<StateEntity> states = new Vector<StateEntity>();
		
		StateEntity state = new StateEntity();
		state.setTitle("OS");
		state.setMessage(System.getProperty("os.name") + ":"
				+ System.getProperty("os.version"));
		state.setType(2);
		states.add(state);

		state = new StateEntity();
		state.setTitle("Java version");
		state.setMessage(System.getProperty("java.version"));
		state.setType(3);
		states.add(state);
		
		state = new StateEntity();
		state.setTitle("Java memory");
		state.setMessage("Totoal: "+ Runtime.getRuntime().totalMemory() + " Free:" + Runtime.getRuntime().freeMemory());
		state.setType(3);
		states.add(state);
		
		state = new StateEntity();
		state.setTitle("Processors");
		state.setMessage("Totoal: "+ Runtime.getRuntime().availableProcessors());
		state.setType(3);
		states.add(state);
		
		state = new StateEntity();
		state.setTitle("Accounts Management");
		state.setMessage(accountManagement.provider());
		state.setType(3);
		states.add(state);

		state = new StateEntity();
		state.setTitle("Roles Management");
		state.setMessage(roleManagement.provider());
		state.setType(3);
		states.add(state);

		state = new StateEntity();
		state.setTitle("Projects Management");
		state.setMessage(projectManagement.provider());
		state.setType(3);
		states.add(state);

		return new GetResults<StateEntity>(states);
	}

	@Override
	public Class<StateAction> getActionType() {
		return StateAction.class;
	}

	@Override
	public void undo(StateAction action, GetResults<StateEntity> result, ExecutionContext context) throws ActionException {
		// do nothing
	}
}
