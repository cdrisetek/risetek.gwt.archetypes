package ${package}.server.accounts;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.accounts.projects.IProjectsManagement;
import ${package}.server.accounts.projects.ProjectActionHandler;
import ${package}.server.accounts.projects.SimpleProjectsManagement;
import ${package}.server.accounts.roles.IRoleManagement;
import ${package}.server.accounts.roles.RoleActionHandler;
import ${package}.server.accounts.roles.SimpleRoleManagement;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.server.shiro.MyAuthorizingRealm;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.accounts.AuthorizationAction;
import ${package}.share.accounts.SubjectAction;
import ${package}.share.accounts.projects.ProjectAction;
import ${package}.share.accounts.roles.RoleAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		// Roles
		bind(IRoleManagement.class).to(SimpleRoleManagement.class).asEagerSingleton();
		bindHandler(RoleAction.class, RoleActionHandler.class);
		// Accounts
		// NOTE: you can change ISubjectManagement to another.
		bind(IAccountManagement.class).to(SimpleAccountManagement.class).asEagerSingleton();
		bind(ISubjectManagement.class).to(SimpleAccountManagement.class).asEagerSingleton();
		bindHandler(AccountAction.class, AccountActionHandler.class);
		// Projects
		bind(IProjectsManagement.class).to(SimpleProjectsManagement.class).asEagerSingleton();
		bindHandler(ProjectAction.class, ProjectActionHandler.class);

		// Authorizing
		bind(IAuthorizingHandler.class).to(MyAuthorizingRealm.class).asEagerSingleton();
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
		bindHandler(AuthenticationAction.class, AuthenticationActionHandler.class);
		bindHandler(AuthorizationAction.class, AuthorizationActionHandler.class);
	}
}
