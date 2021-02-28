package ${package}.server;

import java.util.List;
import java.util.Vector;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.container.StateAction;
import ${package}.share.container.StateEntity;
import ${package}.share.dispatch.GetResults;

public class StateActionHandler implements ActionHandler<StateAction, GetResults<StateEntity>> {
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
