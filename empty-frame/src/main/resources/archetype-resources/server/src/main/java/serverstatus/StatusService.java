package ${package}.serverstatus;

import java.util.List;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * The server side implementation of the GreetingContext.
 */
public class StatusService {

	private final Provider<ServletContext> ctx;
	private final Provider<HttpServletRequest> req;

	@Inject
	StatusService(Provider<ServletContext> ctx, Provider<HttpServletRequest> req) {
		this.ctx = ctx;
		this.req = req;
	}

	
	public List<StatusResponse> statusServer() {
		List<StatusResponse> responseList = new Vector<>();
		StatusResponse response;
		
		response = new StatusResponse();
		response.setTitle("OS");
		response.setMessage(System.getProperty("os.name") + ":"
				+ System.getProperty("os.version"));
		responseList.add(response);

		response = new StatusResponse();
		response.setTitle("Java version");
		response.setMessage(System.getProperty("java.version"));
		responseList.add(response);
		
		response = new StatusResponse();
		response.setTitle("Java memory");
		response.setMessage("Totoal: "+ Runtime.getRuntime().totalMemory() + " Free:" + Runtime.getRuntime().freeMemory());
		responseList.add(response);
		
		response = new StatusResponse();
		response.setTitle("Processors");
		response.setMessage("Totoal: "+ Runtime.getRuntime().availableProcessors());
		responseList.add(response);
		
		response = new StatusResponse();
		response.setTitle("Server Info");
		response.setMessage(ctx.get().getServerInfo());
		responseList.add(response);
		
		response = new StatusResponse();
		response.setTitle("User Agent");
		response.setMessage(req.get().getHeader("User-Agent"));
		responseList.add(response);
		
		return responseList;
	}
}
