package ${package}.serverstatus;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

/**
 * The client side stub for the RequestFactory service.
 */
@ServiceName(value="${package}.serverstatus.StatusService", locator="${package}.GuiceServiceLocator")
public interface ServerStatusContext extends RequestContext {
	Request<List<ResponseProxy>> statusServer();
}
