package ${package};

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.container.StateAction;

public class MyHandlerModule extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(LoginOutAction.class, LogInOutActionHandler.class);
		bindHandler(StateAction.class, StateActionHandler.class);
	}
}
