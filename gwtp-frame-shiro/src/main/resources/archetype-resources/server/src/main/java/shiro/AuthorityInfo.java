package ${package}.shiro;

import java.util.HashMap;
import java.util.Map;

public class AuthorityInfo {
	public Map<String, Boolean> getRoles() {
		return roles;
	}
	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	private Map<String, Boolean> roles = new HashMap<String, Boolean>();
	private String realm;
	private boolean isLogin = false;
	
	public boolean hasRole(String roleName) {
		Boolean value = roles.get(roleName);
		return ((null != value) && (value.booleanValue()));
	}
}
