package ${package}.server.realmgt;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.realmgt.SubjectAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
	}
}
