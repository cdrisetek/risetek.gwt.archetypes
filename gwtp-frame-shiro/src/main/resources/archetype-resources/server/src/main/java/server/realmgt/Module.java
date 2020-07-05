package ${package}.server.realmgt;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.shiro.UserManagement;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.realmgt.SubjectAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// NOTE: you can change ISubjectManagement to another.
		bind(ISubjectManagement.class).to(UserManagement.class).asEagerSingleton();
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
	}
}
