package ${package}.security;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import ${package}.security.email.UpdateEmailModule;
import ${package}.security.password.UpdatePasswordModule;

public class SecurityModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PagePresenter.class, PagePresenter.MyView.class, PageView.class,
                PagePresenter.MyProxy.class);
        
    	install(new UpdatePasswordModule());
    	install(new UpdateEmailModule());
    }
}
