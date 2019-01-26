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
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import ${package}.shiro.MyShiroWebModule;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;

public class ${module}ServletContextListener extends GuiceServletContextListener {
	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
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
				serve("/gwtRequest").with(GuiceRequestFactoryServlet.class);
		        filter("/gwtRequest").through(GuiceShiroFilter.class);
				// ========================================================================
				serve("/freeRequest").with(GuiceRequestFactoryServlet.class);
		        filter("/freeRequest").through(GuiceShiroFilter.class);
				// ========================================================================

				bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);

				bind(Context.class).toInstance(ctx);
/*				
				bind(String.class).annotatedWith(Names.named("${module-short-name}/logoutUrl")).toProvider(
						JndiIntegration.fromJndi(String.class, "${module-short-name}/logoutUrl"));

				bind(User.class).annotatedWith(CurrentUser.class).to(ServerUser.class);
*/
			}
/*
			@Provides @CurrentUser @RequestScoped
			String provideCurrentUser(HttpServletRequest request) {
				return request.getRemoteUser();
			}

			@Provides @IsAdmin @RequestScoped
			boolean provideIsAdmin(HttpServletRequest request) {
				return request.isUserInRole("admin");
			}
*/
		},
			new MyShiroWebModule(servletContext));
	}
}

