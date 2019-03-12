package ${package}.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.risetek.home.cards.state.StateWidgetPresenter;
import com.risetek.home.cards.state.StateWidgetView;
import com.risetek.home.cards.welcome.WelcomeWidgetPresenter;
import com.risetek.home.cards.welcome.WelcomeWidgetView;

public class HomeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        
    	bindSingletonPresenterWidget(WelcomeWidgetPresenter.class,
    			WelcomeWidgetPresenter.MyView.class, WelcomeWidgetView.class);

    	bindSingletonPresenterWidget(StateWidgetPresenter.class,
    			StateWidgetPresenter.MyView.class, StateWidgetView.class);
    }
}
