package ${package}.presentermodules.platformMenu;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;

@AutoLoadPresenterModule
public class PlatformMenuModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class,
        		PagePresenter.MyView.class,
        		ViewImpl.class,
                PagePresenter.MyProxy.class);
        
        bind(AbstractPlatformBarMenu.class).annotatedWith(Names.named("LoginMenu")).to(SimpleLoginMenu.class).in(Singleton.class);
        bind(AbstractPlatformBarMenu.class).annotatedWith(Names.named("NavgatorMenu")).to(SimpleNavMenu.class).in(Singleton.class);
        // bind(NavMenu.class).in(Singleton.class);
    }
}
