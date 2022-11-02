package ${package}.login;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName("${package}.login.LoginResponse")
public interface ResponseProxy extends ValueProxy {
	public String getUsername();
	public void setUsername(String username);
	public String getRoles();
	public void setRoles(String roles);
}