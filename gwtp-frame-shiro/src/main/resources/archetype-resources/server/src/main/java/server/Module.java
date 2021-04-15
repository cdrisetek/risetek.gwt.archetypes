package ${package}.server;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// Binder common UnsecuredSerializableBatchAction and it Handler
		bindHandler(UnsecuredSerializableBatchAction.class, SerializableBatchActionHandler.class);
	}
}
