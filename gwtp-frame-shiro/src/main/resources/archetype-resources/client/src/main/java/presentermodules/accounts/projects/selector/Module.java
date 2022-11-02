package ${package}.presentermodules.accounts.projects.selector;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;

@AutoLoadPresenterModule
public class Module extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        bindPresenterWidget(SelectorWidget.class,
        		SelectorWidget.MyView.class, SelectorView.class);
    }
}
