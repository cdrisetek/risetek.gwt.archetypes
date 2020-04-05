package ${package}.presentermodules.security.newacct;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class NewAcctModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
