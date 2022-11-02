package ${package}.login;

import java.util.Set;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName("${package}.login.AuthorityResponse")
public interface AuthorityResponseProxy extends ValueProxy {
	public Set<String> getRoles();
	public boolean isLogin();
	public String getUsrname();
	public String getEmail();
}