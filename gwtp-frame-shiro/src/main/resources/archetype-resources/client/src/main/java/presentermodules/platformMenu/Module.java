package ${package}.presentermodules.platformMenu;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;

@AutoLoadPresenterModule
public class Module extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class,
        		PagePresenter.MyView.class,
        		PageView.class,
                PagePresenter.MyProxy.class);
        
        bind(AbstractDockMenu.class).annotatedWith(Names.named("LoginMenu")).to(SimpleLoginMenu.class).in(Singleton.class);
        bind(AbstractDockMenu.class).annotatedWith(Names.named("NavgatorMenu")).to(SimpleNavMenu.class).in(Singleton.class);
        // bind(NavMenu.class).in(Singleton.class);
    }
}
