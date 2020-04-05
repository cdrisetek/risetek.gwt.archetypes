package ${package}.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.generator.AutoLoadPresenterModule;
import ${package}.home.cards.simple.SimpleWidgetPresenter;
import ${package}.home.cards.simple.SimpleWidgetView;
import ${package}.home.cards.state.StateWidgetPresenter;
import ${package}.home.cards.state.StateWidgetView;
import ${package}.home.cards.welcome.WelcomeWidgetPresenter;
import ${package}.home.cards.welcome.WelcomeWidgetView;

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
