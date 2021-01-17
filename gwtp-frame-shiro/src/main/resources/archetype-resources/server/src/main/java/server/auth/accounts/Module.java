package ${package}.server.auth.accounts;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.auth.IUserManagement;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.server.shiro.UserManagement;
import ${package}.share.auth.accounts.AccountAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// NOTE: you can change ISubjectManagement to another.
		bind(IUserManagement.class).to(UserManagement.class).asEagerSingleton();
		bindHandler(AccountAction.class, AccountActionHandler.class);
	}
}
