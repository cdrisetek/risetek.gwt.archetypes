package ${package}.server.devops;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.container.StateAction;
import ${package}.share.devops.DevOpsAction;
import ${package}.share.devops.DevOpsTasksAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bind(ServicesManagement.class).asEagerSingleton();
		bindHandler(DevOpsAction.class, DevOpsActionHandler.class);
		bindHandler(DevOpsTasksAction.class, DevOpsTasksActionHandler.class);
		bindHandler(StateAction.class, StateActionHandler.class);
	}
}
