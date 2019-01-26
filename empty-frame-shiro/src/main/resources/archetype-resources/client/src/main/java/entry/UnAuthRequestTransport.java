package ${package}.entry;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

/**
 * Extends {@link DefaultRequestTransport} to handle authenticate disable requests.
 *
 * <b>Implementation note</b>: largely inspired by <a href=
 * "http://code.google.com/p/google-web-toolkit/source/browse/tags/2.4.0/samples/expenses/src/main/java/com/google/gwt/sample/gaerequest/client/GaeAuthRequestTransport.java"
 * ><code>GaeAuthRequestTransport</code></a>.
 */
public class UnAuthRequestTransport extends DefaultRequestTransport {

	public UnAuthRequestTransport() {
		this.setRequestUrl(GWT.getHostPageBaseURL() + "freeRequest");
	}
}

