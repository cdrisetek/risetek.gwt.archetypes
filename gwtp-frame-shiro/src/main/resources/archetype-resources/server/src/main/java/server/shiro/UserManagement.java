package ${package}.server.shiro;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.google.inject.Singleton;
import ${package}.server.realmgt.ISubjectManagement;
import ${package}.share.RbacConstant;
import ${package}.share.realmgt.AccountDescriptionsEntity;
import ${package}.share.realmgt.AccountEntity;

@Singleton
public class UserManagement implements ISubjectManagement {

	/*
	 * Roles:
	 * admin
	 * operator
	 * visitor
	 * maintenance
	 */
	public class AccountRecord {
		public AccountEntity subject;
		String password;
		
		public AccountRecord() {
			subject = new AccountEntity();
			subject.setRoles(new HashSet<>());
			subject.setAccountDescriptions(new AccountDescriptionsEntity());
		}

		public AccountRecord(String name) {
			subject = new AccountEntity();
			subject.setAccountPrincipal(name);
			subject.setRoles(new HashSet<>());
			subject.setAccountDescriptions(new AccountDescriptionsEntity());
		}

		public AccountRecord setName(String name) {
			subject.setAccountPrincipal(name);
			return this;
		}

		public AccountRecord grant(String role) {
			if(RbacConstant.isValidRole(role))
				subject.getRoles().add(role);
			return this;
		}

		public AccountRecord setEmail(String email) {
			subject.getAccountDescriptions().setEmail(email);
			return this;
		}
		
		public AccountRecord setNotes(String notes) {
			subject.getAccountDescriptions().setNotes(notes);
			return this;
		}
		
		public AccountRecord setEnable(boolean enable) {
			subject.setEnable(enable);
			return this;
		}
		
		public AccountRecord setTelphone(String telphone) {
			subject.getAccountDescriptions().setTelphone(telphone);
			return this;
		}

		public AccountRecord setPassword(String password) {
			this.password = password;
			return this;
		}
	}

	private final Map<String, AccountRecord> accounts = new HashMap<>();

	private void tested_users(int index) {
		AccountRecord account = new AccountRecord("admin"+ index);
		account.password = "admin" + index;
		account.setName("admin" + index).grant("admin" + index).grant("developer").grant("maintenance").grant("operator").grant("visitor")
		       .setEmail("admin"+ index + "@risetek.com");
		account.setNotes("account admin" + index);
		account.setEnable(false);
		accounts.put(account.subject.getAccountPrincipal(), account);		
	}

	public UserManagement() {
		AccountRecord account = new AccountRecord("wangyc@risetek.com");
		account.password = "gamelan";
		account.grant("admin").grant("developer").grant("maintenance").grant("operator").grant("visitor").setEmail("wangyc@risetek.com");
		accounts.put(account.subject.getAccountPrincipal(), account);
		
		
		account = new AccountRecord("wangyc");
		account.password = "gamelan";
		account.grant("visitor").setEmail("wangyc@risetek.com");
		accounts.put(account.subject.getAccountPrincipal(), account);		
	
		for(int index =0 ; index < 100; index++)
			tested_users(index);

		// TODO: If no one have admin role, we create admin for this project as default.
		if(accounts.isEmpty()) {
			final AccountRecord defaultUser = new AccountRecord("admin").grant("admin").grant("maintenance");
			defaultUser.password = "admin";
			accounts.put(account.subject.getAccountPrincipal(), defaultUser);
		}
	}
	
	public AccountRecord getUserInfomation(String username) {
		AccountRecord user = accounts.get(username);
		return user;
	}
	
	@RequiresAuthentication
	public Set<String> getRoles(String username) {
		AccountRecord user = accounts.get(username);
		if(null != user)
			return user.subject.getRoles();
		return null;
	}
	
	@RequiresAuthentication
	public void updateSecurity(String username, String password, AccountEntity account) {
		AccountRecord user = accounts.get(username);
		if(null == user)
			return;

		if(null != password)
			user.password = password;

		if(null != account) {
			AccountDescriptionsEntity accountDescription = account.getAccountDescriptions();
			if(null != accountDescription.getEmail())
				user.subject.getAccountDescriptions().setEmail(accountDescription.getEmail());
			if(null != accountDescription.getNotes())
				user.subject.getAccountDescriptions().setEmail(accountDescription.getNotes());
			if(null != accountDescription.getTelphone())
				user.subject.getAccountDescriptions().setEmail(accountDescription.getTelphone());
		}
	}

	private boolean isLike(String like, Map.Entry<String, AccountRecord> entry) {
		if(null == like || null == entry || entry.getKey().indexOf(like) != -1)
			return true;

		AccountRecord account = entry.getValue();
		if(null == account)
			return true;
		
		AccountDescriptionsEntity pe = account.subject.getAccountDescriptions();
		
		if(null != pe.getEmail() && pe.getEmail().indexOf(like) != -1)
			return true;
		
		if(null != pe.getNotes() && pe.getNotes().indexOf(like) != -1)
			return true;

		if(null != pe.getTelphone() && pe.getTelphone().indexOf(like) != -1)
			return true;
		
		return false;
	}
	
	// @RequiresPermissions("realm:listsubjects")
	@RequiresRoles("maintenance")
	@Override
	public List<AccountEntity> ReadSubjects(String like, int offset, int size) {
		List<AccountEntity> subjects = new Vector<AccountEntity>();
		int start = 0;
		int count = 0;
		for(Map.Entry<String, AccountRecord> entry : accounts.entrySet()) {
			if(!isLike(like, entry))
				continue;

			if(start++ < offset)
				continue;
			
			if(count++ > size)
				break;

			subjects.add(entry.getValue().subject);
		}
		return subjects;
	}

	@Override
	@RequiresRoles("admin")
	public boolean CreateSubjects(Set<AccountEntity> subjects, String password) {
		for(AccountEntity subject:subjects) {
			AccountRecord account = new AccountRecord(subject.getAccountPrincipal());
			account.grant("developer").grant("maintenance").grant("operator").grant("visitor")
			        .setPassword(password)
	                .setTelphone(subject.getAccountDescriptions().getTelphone())
	                .setNotes(subject.getAccountDescriptions().getNotes())
			        .setEmail(subject.getAccountDescriptions().getEmail())
			        .setEnable(true);
			accounts.put(subject.getAccountPrincipal(), account);		
		}
		return true;
	}

	@Override
	@RequiresRoles("admin")
	public boolean UpdateSubjects(Set<AccountEntity> subjects) {
		for(AccountEntity subject:subjects) {
			AccountRecord account = accounts.get(subject.getAccountPrincipal());
			if(null == account)
				continue;

			AccountEntity oldacct = account.subject;
			oldacct.setEnable(subject.isEnable());
			
			AccountDescriptionsEntity newDescription = subject.getAccountDescriptions();
			if(null != newDescription) {
				if(null != newDescription.getEmail())
					oldacct.getAccountDescriptions().setEmail(newDescription.getEmail());
				
				if(null != newDescription.getNotes())
					oldacct.getAccountDescriptions().setNotes(newDescription.getNotes());
				
				if(null != newDescription.getTelphone())
					oldacct.getAccountDescriptions().setTelphone(newDescription.getTelphone());
			}
		}
		return true;
	}

	@Override
	public boolean checkValid(AuthenticationToken token) {
		char credentials[] = (char[])token.getCredentials();
		if(null == credentials)
			return false;

		AccountRecord user = accounts.get((String)token.getPrincipal());
		if(null == user || null == user.password)
			return false;

		if(Arrays.equals(credentials, user.password.toCharArray()))
			return true;

		return false;
	}
}
