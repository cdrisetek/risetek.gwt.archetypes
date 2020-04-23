package ${package}.shiro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import com.google.inject.Singleton;
import ${package}.RbacConstant;
import ${package}.realmgt.ISubjectManagement;
import ${package}.realmgt.PrincipalEntity;
import ${package}.realmgt.SubjectEntity;

@Singleton
public class UserManagement implements ISubjectManagement {

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

	private void tested_users(int index) {
		UserInformation userInfo = new UserInformation();
		userInfo.password = "admin" + index;
		userInfo.grant("admin" + index).grant("developer").grant("maintenance").grant("operator").grant("visitor").email("admin@risetek.com");
		users.put("admin" + index, userInfo);		
	}

	public UserManagement() {
		UserInformation userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.grant("admin").grant("developer").grant("maintenance").grant("operator").grant("visitor").email("wangyc@risetek.com");
		users.put("wangyc@risetek.com", userInfo);
		
		
		userInfo = new UserInformation();
		userInfo.password = "gamelan";
		userInfo.grant("visitor").email("wangyc@risetek.com");
		users.put("wangyc", userInfo);		
	
		for(int index =0 ; index < 100; index++)
			tested_users(index);
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

	private boolean isLike(String like, String text) {
		if(null == like)
			return true;
		
		if(null == text)
			return false;

		if(text.indexOf(like) != -1) {
			return true;
		}
		
		return false;
	}
	@Override
	public List<SubjectEntity> ReadSubjects(String like, int offset, int size) {
		List<SubjectEntity> subjects = new Vector<SubjectEntity>();
		int start = 0;
		int count = 0;
		for(Map.Entry<String, UserInformation> entry : users.entrySet()) {
			if(!(isLike(like, entry.getKey()) || isLike(like, entry.getValue().email)))
				continue;

			if(start++ < offset)
				continue;
			
			if(count++ > size)
				break;

			PrincipalEntity principal = new PrincipalEntity();
			principal.setName(entry.getKey());
			principal.setEmail(entry.getValue().email);
			principal.setTelphone(null);
			
			SubjectEntity subject = new SubjectEntity();
			subject.setPrincipal(principal);
			
			subjects.add(subject);
		
		}
		return subjects;
	}

	@Override
	public boolean CreateSubjects(Set<SubjectEntity> subjects) {
		for(SubjectEntity subject:subjects) {
			UserInformation userInfo = new UserInformation();
			userInfo.password = "admin";
			userInfo.grant("developer").grant("maintenance").grant("operator").grant("visitor").email(subject.getPrincipal().getEmail());
			users.put(subject.getPrincipal().getName(), userInfo);		
		}
		return true;
	}
}
