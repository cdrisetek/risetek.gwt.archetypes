package ${package}.server.database;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.IProjectsManagement;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.bindery.AutoLoadModule;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// Roles
		bind(IRoleManagement.class).toProvider(DatabaseProvider.RoleManagement.class).asEagerSingleton();
		// Accounts
		bind(IAccountManagement.class).toProvider(DatabaseProvider.AccountManagement.class).asEagerSingleton();
		// Projects
		bind(IProjectsManagement.class).toProvider(DatabaseProvider.ProjectsManagement.class).asEagerSingleton();
	}
}
