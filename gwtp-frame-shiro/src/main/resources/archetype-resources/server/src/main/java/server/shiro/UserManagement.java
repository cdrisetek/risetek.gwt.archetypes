package ${package}.server.shiro;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.google.inject.Singleton;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.auth.IUserManagement;
import ${package}.share.UniqueID;
import ${package}.share.auth.EnumRBAC;
import ${package}.share.auth.AccountEntity;
import ${package}.share.auth.accounts.EnumAccount;
import ${package}.share.auth.accounts.AccountStateEntity;
import ${package}.share.exception.ActionUnauthenticatedException;

/**
 * 本实现基于内存存储
 * 本实现对于权限(Role)的依赖基于Shiro
 * 对用户信息的修改仅限于用户自身，本实现不允许别人修改用户的信息
 * 考虑到系统初始化时没有初始数据，因此当用户中不存在ADMIN权限时，系统缺省建立admin/admin用户，并具有ADMIN权限
 * 
 * @author wangyc@risetek.com
 *
 */
@Singleton
public class UserManagement implements IUserManagement {

	private static class UserRecorder {
		String username;
		String password;
		public UniqueID ID = new UniqueID();

		public UserRecorder(String username, String password) {
			this.username = username;
			this.password = password;
			setDescription(EnumAccount.PRINCIPAL, username);
			userCollection.put(username, this);
		}

		public UserRecorder(String username, String password, Map<String, String> description) {
			this.username = username;
			this.password = "random";
			descriptions.put(ID, description);
			userCollection.put(username, this);
		}
		
		public AccountEntity getEntity() {
			AccountEntity entity = new AccountEntity();
			entity.setDescriptions(descriptions.get(ID));
			entity.setState(states.get(ID));

			return entity;
		}

		public UserRecorder setDescription(EnumAccount enumUserDescription, String value) {
			Map<String, String> description = Optional.ofNullable(descriptions.get(ID))
					.orElseGet(()->{descriptions.put(ID, new HashMap<>()); return descriptions.get(ID);});

			description.put(enumUserDescription.name(), value);
			return this;
		}

		public UserRecorder setRole(EnumRBAC enumRBAC) {
			Set<String> authorization = Optional.ofNullable(authorizations.get(ID))
					.orElseGet(()->{authorizations.put(ID, new HashSet<>()); return authorizations.get(ID);});
			
			authorization.add(enumRBAC.name().toLowerCase());
			return this;
		}
		
		public UserRecorder setStateEnable(boolean enable) {
			AccountStateEntity state = Optional.ofNullable(states.get(ID))
					.orElseGet(()->{states.put(ID, new AccountStateEntity()); return states.get(ID);});

			state.setEnable(enable);
			return this;
		}

		public boolean isLike(String like) {
			if(null == like)
				return true;
			if(username.indexOf(like) != -1)
				return true;
			
			Map<String, String> description = descriptions.get(ID);
			if((null != description) && description.values().stream().anyMatch(val->val.indexOf(like) != -1))
				return true;
			
			return false;
		}
	}

	private final static Map<String, UserRecorder> userCollection = new TreeMap<>();
	private final static Map<UniqueID, Map<String, String>> descriptions = new HashMap<>();
	private final static Map<UniqueID, AccountStateEntity> states = new HashMap<>();
	private final static Map<UniqueID, Set<String>> authorizations = new HashMap<>();
	
	// 非 OAuth Client模式下，application需要有Role设定。
	// 大多数情况下，Role Set是与application相关的，是可以程序预先设定的。
	// TODO: 
	// 与OAuth Server交互的时候，这个预设的Role Set需要与OAuth Server
	// 中的数据相一致，可以考虑由本应用发起一个数据同步的过程。
	private final static Set<String> roleSet = new HashSet<>();
	
	public UserManagement() {
		// Initial application RoleSet, It should be changed by developer for another project.
		// All role defined in RBAC
		for(EnumRBAC r:EnumRBAC.values())
			addRoleSet(r.name().toLowerCase());

		// Load users data
		loadUsers();
		
		// TODO: If no one have admin role, we create admin for this project as default.
		if(!authorizations.values().stream()
			.anyMatch(author->author
			 .contains(EnumRBAC.ADMIN.name().toLowerCase())))
			new UserRecorder("admin", "admin")
			   .setDescription(EnumAccount.NOTES, "this is the only one")
			   .setRole(EnumRBAC.ADMIN);
	}
	
	// @RequiresPermissions("realm:listsubjects")
	@RequiresRoles(value={"admin", "maintance"}, logical=Logical.OR)
	@Override
	public List<AccountEntity> readUsers(Set<AccountEntity> entities, String like, int offset, int size) throws Exception {
		List<AccountEntity> list = new ArrayList<AccountEntity>();
		if(null != entities) {
			entities.stream().forEach(entity->{
				// TODO: userCollection.get(entity.getDescriptions().get(EnumUserDescription.PRINCIPAL.name())) NULL?
				Optional.ofNullable(userCollection.get(entity.getDescriptions().get(EnumAccount.PRINCIPAL.name()))).ifPresent(e->{
					list.add(e.getEntity());
				});
				// list.add(userCollection.get(entity.getDescriptions().get(EnumUserDescription.PRINCIPAL.name())).getEntity());
			});
			return list;
		}

		int start = 0;
		int count = 0;
		for(UserRecorder user:userCollection.values()) {
			if(!user.isLike(like))
				continue;

			if(start++ < offset)
				continue;

			if(count++ > size)
				break;

			AccountEntity entity = user.getEntity();
			entity.setState(states.get(user.ID));
			list.add(entity);
		}

		return list;
	}

	private UserRecorder getUserRecord(AccountEntity entity) {
		Map<String, String> description = entity.getDescriptions();
		if(null == description)
			return null;
		String principal = description.get(EnumAccount.PRINCIPAL.name());
		if(null == principal)
			return null;
		return userCollection.get(principal);
		
	}
	// 创建新用户的场景出现在注册用户的情况下，如果是OAuth Server，新用户注册还涉及到Project的信息。
	// 新用户创建后，角色(Role)并没有被授予，也就意味着没有任何权限
	// 创建用户的场景也出现在简单的系统构建中，管理员可以主动创建用户，但是密码的处理需要特别考虑，比如通过
	// email进行二次确认？

	@Override
	@RequiresRoles("admin")
	public void createUser(AccountEntity user, String password) throws Exception {
		Map<String, String> description = user.getDescriptions();
		if(null == description)
			return;
		String principal = description.get(EnumAccount.PRINCIPAL.name());
		if(null == principal)
			return;
		
		new UserRecorder(principal, password, description);
	}

	@Override
	@RequiresRoles("admin")
	public void createUser(AccountEntity user) throws Exception {
		// TODO: Notice user to change password somehow.
		String password = "random";
		createUser(user, password);
	}
	
	@Override
	@RequiresRoles(value={"admin", "maintance"}, logical=Logical.OR)
	public void setUserState(AccountEntity user, AccountStateEntity state) throws Exception {
		Optional.ofNullable(getUserRecord(user)).ifPresent(u->{
			states.get(u.ID).setEnable(state.isEnable());
		});
	}
	
	// HasGod interface implement
	@Override
	@RequiresRoles("admin")
	public void updateUsers(Set<AccountEntity> users) throws Exception {
		users.stream().forEach(user->{

			Optional.ofNullable(getUserRecord(user)).ifPresent(u->{
				Map<String, String> description =
				Optional.ofNullable(descriptions.get(u.ID)).orElseGet(()->{
					descriptions.put(u.ID, new HashMap<>());
					return descriptions.get(u.ID);
				});
				
				user.getDescriptions().entrySet().stream().forEach(entry->{
					description.put(entry.getKey(), entry.getValue());
				});

				if(null != user.getState())
					states.get(u.ID).setEnable(user.getState().isEnable());
				
			});
		});
	}
	
	@Override
	@RequiresAuthentication
	public void setUserPassword(AccountEntity user, String password) {
		// TODO: only current user can update himself User Informations.
	}
	// End of HasGod interface implement

	// HasSubject interface implements
	@RequiresAuthentication
	@Override
	public Set<String> getSubjectRoles() throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		UniqueID id = (UniqueID)currentUser.getPrincipal();
		return authorizations.get(id);
	}
	
	@RequiresAuthentication
	@Override
	public AccountEntity getSubjectUserEntity() throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		UniqueID id = (UniqueID)currentUser.getPrincipal();
		AccountEntity entity = new AccountEntity();
		entity.setDescriptions(descriptions.get(id));
		return entity;
	}
	
	@RequiresAuthentication
	@Override
	public void setSubjectUserPassword(String password) throws ActionException {
		if(null == password || password.isEmpty()) {
			System.out.println("resetCurrentUserPassword");
			// TODO: should send email for this.
			password = "randomresetpassword";
		}

		Subject subject = SecurityUtils.getSubject();
		UniqueID id = (UniqueID)subject.getPrincipal();
		String username = descriptions.get(id).get(EnumAccount.PRINCIPAL.name());
		if(null == username)
			throw new ActionException("can't find subject user name. this should not happen.");
		UserRecorder user = userCollection.get(username);
		if(null == user)
			throw new ActionException("can't find subject user. this should not happen.");

		user.password = password;
		
		// Logout and force user login with new password again.
		subject.logout();
		throw new ActionUnauthenticatedException();
	}
	// End of HasSubject interface implements
	// HasAuthentication interface implements
	@Override
	public UniqueID authenticate(String principal, char[] credentials) throws Exception {
		if((null == principal) || (null == credentials))
			throw new Exception("miss authentication data");

		UserRecorder user = userCollection.get(principal);
		if(null == user || null == user.password)
			throw new Exception("miss user");

		if(!Arrays.equals(credentials, user.password.toCharArray()))
			throw new Exception("wrong credentials");
		
		return user.ID;
	}
	// End of HasAuthentication interface implements.
	
	// HasAuthorization interface implements
	@RequiresAuthentication
	@Override
	public Set<String> getUserRoles(UniqueID id) {
		return Optional.ofNullable(authorizations.get(id))
				.orElseGet(()->{authorizations.put(id, new HashSet<>()); return authorizations.get(id);});
	}

	@Override
	public UserManagement addRoleSet(String role) {
		roleSet.add(role);
		return this;
	}
	// End of HasAuthorization interface implements.
	
	// Test
	private static void loadUsers() {
		for(int index =0 ; index < 100; index++) {
			String username = String.format("admin%03d@risetek.com", index);
			UserRecorder user = new UserRecorder(username, "admin");
			user.setStateEnable(true)
			    .setDescription(EnumAccount.EMAIL, username)
			    .setRole(EnumRBAC.MAINTANCE);
			if(index % 2 == 0)
				user.setDescription(EnumAccount.NOTES, "this is a very long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long notes");
			else
				user.setDescription(EnumAccount.NOTES,"this is short notes.");
			
		}

		String username = "wangyc@risetek.com";
		new UserRecorder(username, "gamelan")
		   .setStateEnable(true)
		   .setDescription(EnumAccount.EMAIL, username)
		   .setDescription(EnumAccount.NOTES, "it's me")
		   .setRole(EnumRBAC.ADMIN);
	}
}
