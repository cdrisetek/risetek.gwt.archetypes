package ${package}.server.database.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.IAuthorizingHandler;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.devops.DevOpsTask;
import ${package}.server.devops.ServicesManagement;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.container.StateEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

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
public class SimpleAccountManagement implements IAccountManagement {
	@Inject private IAuthorizingHandler authorizing;
	@Inject private IRoleManagement roleManagement;
	@Inject
	public SimpleAccountManagement() {
		new DevOpsTask("SimpleAccountManagement account scanner", TaskType.AUTHOR, (t) -> {

			// TODO: check anybody get ADMIN role, if no one, this is deadlock.
			if(anyMatchedRole("DEVELOPER")) {
				t.stat = TaskState.READY;
			} else {
				t.stat = TaskState.FAILED;
			}
		});
		
		ServicesManagement.provideState(() -> {
			StateEntity state = new StateEntity();
			state.setTitle("Accounts Management");
			state.setMessage("Memory based SimpleAccountManagement");
			state.setType(3);
			return state;
		});
	}
	
	private class AccountRecord {
		String username;
		String password;

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
	
	@Override
	public List<AccountEntity> readAccounts(Set<AccountEntity> entities, String like, int offset, int size) throws ActionException {
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
	public void createAccount(AccountEntity accountEntity, String password) throws ActionException {
		String principal = accountEntity.getPrincipal();
		if(null == principal)
			return;

		new AccountRecord(principal, password, accountEntity.getDescriptions());
	}

	@Override
	public void updateAccounts(Set<AccountEntity> users) throws ActionException {
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

	@Override
	public Object getCredential(Object principal) throws ActionException {
		AccountRecord account = accountSTORE.get(principal);
		if(null == account)
			throw new ActionException("no such account");
		return account.password;
	}

	@Override
	public void setCredential(Object principal, Object credential) throws ActionException {
		AccountRecord account = accountSTORE.get(principal);
		if(null == account)
			throw new ActionException("can't find subject user. this should not happen.");

		account.password = authorizing.encryptRealmPassword((String)credential);
	}

	@Override
	public AccountEntity getAccount(Object principal) throws ActionException {
		AccountRecord account = accountSTORE.get(principal);
		if(null == account)
			throw new ActionException("can't find subject user. this should not happen.");
		return account.getEntity();
	}

	@Override
	public boolean anyMatchedRole(String role) {
		return accountSTORE.keySet().stream().anyMatch(name -> {
				try {
					return roleManagement.getRoleSet(name, null).stream().anyMatch(r -> r.equals(role));
				} catch (ActionException e) {
					return false;
				}
		});
	}
}
