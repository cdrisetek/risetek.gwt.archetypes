package ${package};

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.shiro.guice.web.GuiceShiroFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import ${package}.servlet.LoginServlet;
import ${package}.shiro.MyShiroWebModule;

public class AppServletContextListener extends GuiceServletContextListener {
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
	
	@Override
	protected Injector getInjector() {
		final Context ctx;
		try {
			InitialContext ic = new InitialContext();
			ctx = (Context) ic.lookup("java:comp/env");
		} catch (NamingException ne) {
			throw new RuntimeException(ne);
		}

		return Guice.createInjector(
				new ServletModule() {
					@Override
					protected void configureServlets() {
						serve("/login").with(LoginServlet.class);
						serve("/dispatch/*").with(DispatchServiceImpl.class);
						//shiro filter
				        filter("/dispatch/*").through(GuiceShiroFilter.class);
						// ========================================================================

//						bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
		
						bind(Context.class).toInstance(ctx);
					}
				},
				new MyShiroWebModule(servletContext),
				// TODO: Guice inject please.
				new ${package}.realmgt.Module(),
				new ${package}.MyHandlerModule());
	}
}

