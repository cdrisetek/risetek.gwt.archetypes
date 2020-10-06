package ${package}.server;

import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.GuiceShiroFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import ${package}.server.servlet.LoginServlet;
import ${package}.server.shiro.MyShiroWebModule;
import ${package}.server.servlet.OAuthJWTServlet;

/**
 * Don't change this class, Maven-processer-plugin need to override this class for auto load submodules.
 * 
 * @author wangyc@risetek.com
 */

public abstract class AppServletContextListener extends GuiceServletContextListener {
	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}	

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
	}
	
	private final List<Module> modulesList = new Vector<>();
	
	public abstract void appendModules(List<Module> list);
	
	@Override
	protected Injector getInjector() {
		final Context ctx;
		try {
			InitialContext ic = new InitialContext();
			ctx = (Context) ic.lookup("java:comp/env");
		} catch (NamingException ne) {
			throw new RuntimeException(ne);
		}

		modulesList.add(new ServletModule() {
			@Override
			protected void configureServlets() {
				// For redirect login page to AAA server
				// AAA server could be local or remote OAuth server.
				serve("/login").with(LoginServlet.class);
				// For remote server exchange JWT message with OAuth server.
				serve("/oauth/jwt").with(OAuthJWTServlet.class);

				serve("/dispatch/*").with(DispatchServiceImpl.class);
				//shiro filter
		        filter("/dispatch/*").through(GuiceShiroFilter.class);
//				bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
				bind(Context.class).toInstance(ctx);
			}
		});
		// Install this module to enable Shiro AOP functionality in Guice.
		modulesList.add(new ShiroAopModule());
		
		// @AutoLoadModule loading.
		appendModules(modulesList);
		modulesList.add(new MyShiroWebModule(servletContext));
		return Guice.createInjector(modulesList);
	}
}

