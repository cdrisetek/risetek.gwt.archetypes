package ${package}.realmgt;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.share.realmgt.SubjectAction;

public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
	}
}
