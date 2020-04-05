package ${package}.security;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.generator.AutoLoadPresenterModule;
import ${package}.security.email.UpdateEmailModule;
import ${package}.security.newacct.NewAcctModule;
import ${package}.security.password.UpdatePasswordModule;
import ${package}.security.resetpassword.ResetPasswordModule;

@AutoLoadPresenterModule
public class SecurityModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        
    	install(new UpdatePasswordModule());
    	install(new UpdateEmailModule());
    	install(new NewAcctModule());
    	install(new ResetPasswordModule());
    }
}
