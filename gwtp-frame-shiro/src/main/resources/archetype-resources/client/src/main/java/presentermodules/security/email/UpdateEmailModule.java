package ${package}.presentermodules.security.email;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UpdateEmailModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
