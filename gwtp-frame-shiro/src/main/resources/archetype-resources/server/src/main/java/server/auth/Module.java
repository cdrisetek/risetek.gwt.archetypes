package ${package}.server.auth;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.auth.SubjectAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
	}
}
