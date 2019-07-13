package ${package}.shiro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.inject.Singleton;
import ${package}.RbacConstant;

@Singleton
public class UserManagement {

	/*
	 * Roles:
	 * admin
	 * operator
	 * visitor
	 * maintenance
	 */
	public class UserInformation {
		public String email;
		String password;
		private Set<String> roles = new HashSet<String>();
		
		public UserInformation grant(String role) {
			if(RbacConstant.isValidRole(role))
				roles.add(role);
			return this;
		}

		public UserInformation email(String email) {
			this.email = email;
			return this;
		}
	}

	private final Map<String, UserInformation> users = new HashMap<>();

	public UserManagement() {
		UserInformation userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.grant("admin").grant("developer").grant("maintenance").grant("operator").grant("visitor").email("wangyc@risetek.com");
		users.put("wangyc@risetek.com", userInfo);
		
		
		userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.grant("visitor").email("wangyc@risetek.com");
		users.put("wangyc", userInfo);		
	}
	
	public UserInformation getUserInfomation(String username) {
		UserInformation user = users.get(username);
		return user;
	}
	
	public boolean isValid(String username, char[] password) {
		UserInformation user = users.get(username);
		if(null == user || null == password || null == user.password) {
			return false;
		}
		
		if(Arrays.equals(password, user.password.toCharArray()))
			return true;

		return false;
	}

	public Set<String> getRoles(String username) {
		UserInformation user = users.get(username);
		if(null != user)
			return user.roles;
		return null;
	}
	
	// TODO: password email etc. should be managed as key-value attributes.
	
	public void updatePassword(String username, String newpassword) {
		UserInformation user = users.get(username);
		if(null == user)
			return;
		user.password = newpassword;
	}
	
	public void updateEmail(String username, String newemail) {
		UserInformation user = users.get(username);
		if(null == user)
			return;
		user.email = newemail;
	}
	
	public void updateSecurity(String username, Map<String, String> security) {
		for(Entry<String, String> entry:security.entrySet()) {
			if("password".equals(entry.getKey()))
				updatePassword(username, entry.getValue());
			else if("email".equals(entry.getKey()))
				updateEmail(username, entry.getValue());
		}
	}
}
