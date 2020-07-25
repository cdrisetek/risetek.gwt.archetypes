package ${package}.presentermodules.errorplace;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ErrorModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
