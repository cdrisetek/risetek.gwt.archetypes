package ${package}.server;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.SecurityAction;
import ${package}.share.container.StateAction;

@AutoLoadModule
public class MyHandlerModule extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SecurityAction.class, SecurityActionHandler.class);
		bindHandler(StateAction.class, StateActionHandler.class);
	}
}
