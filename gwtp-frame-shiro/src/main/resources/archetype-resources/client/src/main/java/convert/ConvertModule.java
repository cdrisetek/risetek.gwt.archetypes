package ${package}.convert;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConvertModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
