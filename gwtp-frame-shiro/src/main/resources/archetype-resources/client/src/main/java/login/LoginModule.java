package ${package}.login;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;

@AutoLoadPresenterModule
public class LoginModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
    }
}
