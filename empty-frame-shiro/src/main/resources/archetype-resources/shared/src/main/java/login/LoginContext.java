package ${package}.login;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

/**
 * The client side stub for the RequestFactory service.
 */
@ServiceName(value="${package}.login.LoginService", locator="${package}.GuiceServiceLocator")
public interface LoginContext extends RequestContext {
	Request<AuthorityResponseProxy> login(String username, String passwd);
	Request<Void> logout();
}
