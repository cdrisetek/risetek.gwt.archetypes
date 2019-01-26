package ${package};

import ${package}.login.LoginContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface FreeFactory extends RequestFactory {
	LoginContext loginContext();
}