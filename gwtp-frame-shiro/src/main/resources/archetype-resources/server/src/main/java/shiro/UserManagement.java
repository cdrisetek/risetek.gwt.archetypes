package ${package}.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Singleton;

@Singleton
public class UserManagement {
	public final List<String> allRoles = Stream.of("admin", "maintenance", "operator", "visitor", "developer").collect(Collectors.toCollection(ArrayList::new));

	/*
	 * Roles:
	 * admin
	 * operator
	 * visitor
	 * maintenance
	 */
	public class UserInformation {
		String email;
		String password;
		Set<String> roles = new HashSet<String>();
	}

	private final Map<String, UserInformation> users = new HashMap<>();

	public UserManagement() {
		UserInformation userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.roles.add("admin");
		userInfo.roles.add("developer");
		userInfo.roles.add("maintenance");
		userInfo.roles.add("operator");
		userInfo.roles.add("visitor");
		users.put("wangyc@risetek.com", userInfo);
		
		
		userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.roles.add("visitor");
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
}
