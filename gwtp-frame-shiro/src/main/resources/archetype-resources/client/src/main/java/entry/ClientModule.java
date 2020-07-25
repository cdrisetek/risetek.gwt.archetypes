package ${package}.entry;

import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import ${package}.NameTokens;
import ${package}.root.RootModule;
import ${package}.utils.ServerExceptionHandler;
import ${package}.presentermodules.errorplace.ErrorModule;

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
        install(new ErrorModule());
        install(new RootModule());
        bind(ServerExceptionHandler.class).asEagerSingleton();
    	bind(MyBootstrapper.class).asEagerSingleton();
        bind(CurrentUser.class).asEagerSingleton();
        bind(LoggedInGatekeeper.class);
    }
}
