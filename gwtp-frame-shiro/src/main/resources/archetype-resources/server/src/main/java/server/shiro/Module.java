package ${package}.server.shiro;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.auth.AuthenticationAction;
import ${package}.share.auth.AuthorizationAction;

@AutoLoadModule
public class Module extends HandlerModule {
	
	@Override
	protected void configureHandlers() {
		bindHandler(AuthenticationAction.class, AuthenticationActionHandler.class);
		bindHandler(AuthorizationAction.class, AuthorizationActionHandler.class);
	}
}
