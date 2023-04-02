package ${package}.entry;

import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import ${package}.NameTokens;
import ${package}.utils.ServerExceptionHandler;
import ${package}.websocketevents.EventsBus;

public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
    	// System Special
        // install(new DefaultModule());
    	install(new RpcDispatchAsyncModule());
    	install(new DefaultModule.Builder().tokenFormatter(RouteTokenFormatter.class).build());
        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.login);

        // Application special
        bind(ServerExceptionHandler.class).asEagerSingleton();
    	bind(MyBootstrapper.class).asEagerSingleton();
        bind(Subject.class).asEagerSingleton();
        bind(EventsBus.class).asEagerSingleton();
        bind(LoggedInGatekeeper.class);
    }
}
