package ${package}.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class MyShiroWebModule extends ShiroWebModule {
    public static final Key<MyAuthenticationFilter> MyAUTHC = Key.get(MyAuthenticationFilter.class);

	@Inject
    public MyShiroWebModule(ServletContext sc) {
        super(sc);
    }

	protected void configureShiroWeb() {
    	bindRealm().to(MyAuthorizingRealm.class).asEagerSingleton();
    	
        bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);
        bindConstant().annotatedWith(Names.named("securityManager.rememberMeManager.cipherKey")).to("risetek");
//        addFilterChain("/gwtRequest", MyAUTHC);
    }
}
