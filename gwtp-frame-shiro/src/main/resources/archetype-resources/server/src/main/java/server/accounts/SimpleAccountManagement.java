package ${package}.server.accounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import com.google.inject.Singleton;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.roles.IRoleManagement;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.share.accounts.hosts.HostProjectRBAC;
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
public class SimpleAccountManagement implements IAccountManagement, ISubjectManagement {
	private final IAuthorizingHandler authorizing;
	private final IRoleManagement roleManagement;
	@Inject
	public SimpleAccountManagement(final IRoleManagement roleManagement, final IAuthorizingHandler authorizing) {
		this.roleManagement = roleManagement;
		this.authorizing = authorizing;
		// Load users data
		for(int index =0 ; index < 100; index++) {
			String username = String.format("admin%03d@risetek.com", index);
			AccountRecord user = new AccountRecord(username, "admin")
					               .set(EnumAccount.EMAIL, username)
					               .setRole(HostProjectRBAC.GUEST);
			if(index % 2 == 0)
				user.set(EnumAccount.NOTES, "this is a very long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long long "
						+ "long long long long long long long long long long long long notes");
			else
				user.set(EnumAccount.NOTES,"this is short notes.");
			
		}

		String username = "wangyc@risetek.com";
		new AccountRecord(username, "gamelan")
		   .set(EnumAccount.EMAIL, username)
		   .set(EnumAccount.NOTES, "it's me")
		   .setRole(HostProjectRBAC.DEVELOPER).setRole(HostProjectRBAC.GUEST);
		
		// TODO: If no one have admin role, we create admin for this project as default.
	}

	// ISubjectManagement interface implements
	// TODO: should let shiro handler subject?
	@Override
	public Object getCredential(Object principal) throws Exception {
		AccountRecord account = accountSTORE.get(principal);
		if(null == account)
			throw new Exception("no such account");
		return account.password;
	}

	@RequiresAuthentication
	@Override
	public AccountEntity getSubjectEntity() throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		Object principal = currentUser.getPrincipal();
		AccountEntity entity = new AccountEntity();
		entity.setPrincipal((String)principal);
		entity.setDescriptions(descriptionSTORE.get(principal));
		return entity;
	}
	
	@RequiresAuthentication
	@RequiresPermissions("subject:update")
	@Override
	public void setSubjectPassword(String password) throws ActionException {
		if(null == password || password.isEmpty()) {
			System.out.println("resetCurrentUserPassword");
			// TODO: should send email for this.
			password = "randomresetpassword";
		}

		Subject subject = SecurityUtils.getSubject();
		Object princial = subject.getPrincipal();
		String username = accountSTORE.get(princial).username;
		if(null == username)
			throw new ActionException("can't find subject user name. this should not happen.");
		AccountRecord user = accountSTORE.get(username);
		if(null == user)
			throw new ActionException("can't find subject user. this should not happen.");

		user.password = authorizing.encryptRealmPassword(password);
		
		// Logout and force user login with new password again.
		subject.logout();
		throw new ActionUnauthenticatedException();
	}
	// End of ISubjectManagement interface implements
	
	private class AccountRecord {
		String username;
		String password;

		AccountRecord(String username, String password) {
			this(username, password, null);
		}

		AccountRecord(String username, String password, Map<String, String> description) {
			this.username = username;
			this.password = authorizing.encryptRealmPassword(password);
			descriptionSTORE.put(username, description);
			accountSTORE.put(username, this);
		}
		
		AccountEntity getEntity() {
			AccountEntity entity = new AccountEntity();
			entity.setPrincipal(username);
			entity.setDescriptions(descriptionSTORE.get(username));

			return entity;
		}

		AccountRecord set(EnumAccount enumUserDescription, String value) {
			Map<String, String> description = Optional.ofNullable(descriptionSTORE.get(username))
					.orElseGet(()->{descriptionSTORE.put(username, new HashMap<>()); return descriptionSTORE.get(username);});

			description.put(enumUserDescription.name(), value);
			return this;
		}

		AccountRecord setRole(HostProjectRBAC role) {
			Set<HostProjectRBAC> roles = roleManagement.getRoleSet(username);
			roles.add(role);
			roleManagement.setRoleSet(username, roles);
			return this;
		}
		
		boolean isLike(String like) {
			if(null == like || username.indexOf(like) != -1)
				return true;
			
			Map<String, String> description = descriptionSTORE.get(username);
			if((null != description) && description.values().stream().anyMatch(val->val.indexOf(like) != -1))
				return true;
			
			return false;
		}
	}

	private final static Map<Object, AccountRecord> accountSTORE = new TreeMap<>();
	private final static Map<Object, Map<String, String>> descriptionSTORE = new HashMap<>();
	
	@RequiresPermissions("accounts:read")
	@Override
	public List<AccountEntity> readAccounts(Set<AccountEntity> entities, String like, int offset, int size) throws Exception {
		List<AccountEntity> list = new ArrayList<AccountEntity>();
		if(null != entities) {
			entities.stream().forEach(entity ->
				Optional.ofNullable(accountSTORE.get(entity.getPrincipal())).ifPresent(e -> list.add(e.getEntity())));
			return list;
		}

		int start = 0, count = 0;
		for(AccountRecord account:accountSTORE.values()) {
			if(!account.isLike(like) || start++ < offset)
				continue;

			if(count++ >= size)
				break;

			AccountEntity entity = account.getEntity();
			list.add(entity);
		}

		return list;
	}

	// 创建新用户的场景出现在注册用户的情况下，如果是OAuth Server，新用户注册还涉及到Project的信息。
	// 新用户创建后，角色(Role)并没有被授予，也就意味着没有任何权限
	// 创建用户的场景也出现在简单的系统构建中，管理员可以主动创建用户，但是密码的处理需要特别考虑，比如通过
	// email进行二次确认？

	@Override
	@RequiresPermissions("accounts:create")
	public void createAccount(AccountEntity accountEntity, String password) throws Exception {
		String principal = accountEntity.getPrincipal();
		if(null == principal)
			return;

		new AccountRecord(principal, password, accountEntity.getDescriptions());
	}

	// HasGod interface implement
	@Override
	@RequiresPermissions("accounts:update")
	public void updateAccounts(Set<AccountEntity> users) throws Exception {
		users.stream().forEach(user -> {
			Optional.ofNullable(accountSTORE.get(user.getPrincipal())).ifPresent(u -> {
				Map<String, String> description = Optional.ofNullable(descriptionSTORE.get(u.username)).orElseGet(() -> {
					descriptionSTORE.put(u.username, new HashMap<>());
					return descriptionSTORE.get(u.username);
				});

				Optional.of(user.getDescriptions()).ifPresent(newDescriptions -> {
					// Split input descriptions Map into null-value-map and none-null-value-map.
					Map<Boolean, Map<String, String>> groups = newDescriptions.entrySet().stream()
							.collect(Collectors.partitioningBy(e -> (null == e.getValue() || e.getValue().isEmpty()),
							         Collectors.toMap(e -> e.getKey(), e -> null == e.getValue() ? "" : e.getValue() )));
					
					// Update descriptions items when key with none-null-value
					description.putAll(groups.get(Boolean.valueOf(false /* value not empty */)));

					// Remove descriptions items when key with null-value.
					Optional.of(groups.get(Boolean.valueOf(true /* value is empty */)))
					  .ifPresent(m -> m.entrySet().stream().forEach(e -> description.remove(e.getKey())));
				});
			});
		});
	}
	// End of HasGod interface implement

	@Override
	public void shutdown() {
		System.out.println("Shutdown AccountManagement, if have Database connections, unlock it.");
	}

	@Override
	public String provider() {
		return "Memory based SimpleAccountManagement";
	}
}
