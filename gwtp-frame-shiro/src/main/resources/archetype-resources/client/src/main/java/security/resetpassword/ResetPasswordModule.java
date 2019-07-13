package ${package}.security.resetpassword;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ResetPasswordModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
