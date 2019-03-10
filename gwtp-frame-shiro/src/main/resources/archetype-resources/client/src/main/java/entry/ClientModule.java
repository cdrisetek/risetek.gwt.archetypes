package ${package}.entry;

import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import ${package}.NameTokens;
import ${package}.home.HomeModule;
import ${package}.login.LoginModule;
import ${package}.platformMenu.PlatformMenuModule;
import ${package}.root.RootModule;
import ${package}.convert.ConvertModule;


public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
    	// System Special
        install(new DefaultModule());
    	install(new RpcDispatchAsyncModule());
        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.home);

        // Application special
        install(new RootModule());
        install(new PlatformMenuModule());
    	install(new HomeModule());
    	install(new LoginModule());
    	install(new ConvertModule());
    	
        bind(CurrentUser.class).asEagerSingleton();
        bind(LoggedInGatekeeper.class);
    }
}
