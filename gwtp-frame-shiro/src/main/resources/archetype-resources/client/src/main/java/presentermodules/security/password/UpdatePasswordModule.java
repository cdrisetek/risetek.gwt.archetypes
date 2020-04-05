package ${package}.presentermodules.security.password;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UpdatePasswordModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
