package ${package}.presentermodules.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;
import ${package}.presentermodules.home.cards.simple.SimpleWidgetPresenter;
import ${package}.presentermodules.home.cards.simple.SimpleWidgetView;
import ${package}.presentermodules.home.cards.state.StateWidgetPresenter;
import ${package}.presentermodules.home.cards.state.StateWidgetView;
import ${package}.presentermodules.home.cards.welcome.WelcomeWidgetPresenter;
import ${package}.presentermodules.home.cards.welcome.WelcomeWidgetView;

@AutoLoadPresenterModule
public class HomeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        
    	bindSingletonPresenterWidget(WelcomeWidgetPresenter.class,
    			WelcomeWidgetPresenter.MyView.class, WelcomeWidgetView.class);

    	bindSingletonPresenterWidget(StateWidgetPresenter.class,
    			StateWidgetPresenter.MyView.class, StateWidgetView.class);

    	bindSingletonPresenterWidget(SimpleWidgetPresenter.class,
    			SimpleWidgetPresenter.MyView.class, SimpleWidgetView.class);

    }
}
