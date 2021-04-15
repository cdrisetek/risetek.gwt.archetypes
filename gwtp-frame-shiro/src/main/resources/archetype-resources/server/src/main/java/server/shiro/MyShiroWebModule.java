package ${package}.server.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.google.inject.Inject;
import com.google.inject.name.Names;

public class MyShiroWebModule extends ShiroWebModule {

	@Inject
    public MyShiroWebModule(ServletContext sc) {
        super(sc);
    }

	protected void configureShiroWeb() {
    	bindRealm().to(MyAuthorizingRealm.class).asEagerSingleton();
        bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30*60*1000L /* ms */);
        bindConstant().annotatedWith(Names.named("securityManager.rememberMeManager.cipherKey")).to("risetek");
        // NOTE: AppServletContextListener configure handlers for filters.
    }
}
