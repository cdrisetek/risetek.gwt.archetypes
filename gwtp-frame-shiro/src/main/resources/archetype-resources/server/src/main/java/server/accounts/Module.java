package ${package}.server.accounts;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
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
		bindHandler(RoleAction.class, RoleActionHandler.class);
		// Accounts
		bindHandler(AccountAction.class, AccountActionHandler.class);
		// Projects
		bindHandler(ProjectAction.class, ProjectActionHandler.class);

		// Authorizing
		bind(IAuthorizingHandler.class).to(MyAuthorizingRealm.class).asEagerSingleton();
		bind(ISubjectManagement.class).to(MyAuthorizingRealm.class).asEagerSingleton();
		bind(TemporaryAccount.class).toProvider(TemporaryAccount.deployAccountProvider.class);
		bindHandler(SubjectAction.class, SubjectActionHandler.class);
		bindHandler(AuthenticationAction.class, AuthenticationActionHandler.class);
		bindHandler(AuthorizationAction.class, AuthorizationActionHandler.class);
		
		bind(AccessTokenManagement.class);
	}
}
