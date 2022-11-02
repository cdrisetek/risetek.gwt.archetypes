package ${package}.presentermodules.security;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.bindery.AutoLoadPresenterModule;
import ${package}.presentermodules.security.newacct.NewAcctModule;
import ${package}.presentermodules.security.resetpassword.ResetPasswordModule;

@AutoLoadPresenterModule
public class Module extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        
    	install(new NewAcctModule());
    	install(new ResetPasswordModule());
    }
}
