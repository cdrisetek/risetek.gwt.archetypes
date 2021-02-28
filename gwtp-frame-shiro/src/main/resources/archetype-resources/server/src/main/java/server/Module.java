package ${package}.server;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.container.StateAction;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(StateAction.class, StateActionHandler.class);

		// Binder common UnsecuredSerializableBatchAction and it Handler
		bindHandler(UnsecuredSerializableBatchAction.class, SerializableBatchActionHandler.class);
	}
}
