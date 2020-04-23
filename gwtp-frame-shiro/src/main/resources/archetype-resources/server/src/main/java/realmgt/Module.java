package ${package}.realmgt;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;

public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
	}
}
