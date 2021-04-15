package ${package}.server;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.AdviceFilter;

@Singleton
public class HttpHeaderFilter extends AdviceFilter {

    @Override
    public boolean preHandle(ServletRequest req, ServletResponse res) throws IOException, ServletException {
    	if(req instanceof HttpServletRequest && res instanceof HttpServletResponse)
    		doHttpFilter((HttpServletRequest)req, (HttpServletResponse)res);
		// return true if the filter chain should be allowed to continue.
		return true;
    }

    private final String pattern = "^/(?!dispatch/|.*\\.nocache\\.js|oauth/|demo/oauth).*$";
    private final Pattern nocachePattern = Pattern.compile(pattern);

    private void doHttpFilter(HttpServletRequest request, HttpServletResponse response) {
		if(!nocachePattern.matcher(request.getServletPath()).find()) {
			System.out.println("no cache path: " + request.getServletPath());
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		} else {
			System.out.println("cache path: " + request.getServletPath());
			response.setHeader("Cache-Control", "max-age=31536000, immutable");
		}
			
		response.setHeader("X-Content-Type-Options", "nosniff");
    }
}
