package ${package}.root;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class RootModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(RootPresenter.class,
        		RootPresenter.MyView.class,
        		ViewImpl.class,
                RootPresenter.MyProxy.class);
    }
}
