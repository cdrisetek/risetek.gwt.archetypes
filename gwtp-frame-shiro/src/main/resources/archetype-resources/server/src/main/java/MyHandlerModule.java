package ${package};

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.container.StateAction;

public class MyHandlerModule extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SecurityAction.class, SecurityActionHandler.class);
		bindHandler(StateAction.class, StateActionHandler.class);
	}
}
