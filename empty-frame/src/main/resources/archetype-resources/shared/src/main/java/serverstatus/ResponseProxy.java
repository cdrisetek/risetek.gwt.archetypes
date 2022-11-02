package ${package}.serverstatus;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName("${package}.serverstatus.StatusResponse")
public interface ResponseProxy extends ValueProxy {
	public String getTitle();
	public void setTitle(String title);
	public String getMessage();
	public void setMessage(String message);
}