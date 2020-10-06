package ${package}.server.users;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.auth.IUserManagement;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.server.shiro.UserManagement;
import ${package}.share.users.UserAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// NOTE: you can change ISubjectManagement to another.
		bind(IUserManagement.class).to(UserManagement.class).asEagerSingleton();
		bindHandler(UserAction.class, UserActionHandler.class);
	}
}
