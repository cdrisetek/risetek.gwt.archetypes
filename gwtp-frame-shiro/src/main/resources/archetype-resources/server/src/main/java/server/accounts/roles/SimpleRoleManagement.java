package ${package}.server.accounts.roles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Singleton;

import ${package}.share.templates.Project;

@Singleton
public class SimpleRoleManagement implements IRoleManagement {
	@Override
	public Set<String> getRoleSet(Object account, Object project) {
		Object key = makeKey(account, project);
		return Optional.ofNullable(roleMap.get(key)).orElse(new HashSet<>());
	}

	@Override
	public void setRoleSet(Object account, Object project, Set<String> roles) {
		Object key = makeKey(account, project);
		roleMap.put(key, roles);
	}

	private Map<Object, Set<String>> roleMap = new HashMap<>();
	
	private String makeKey(Object principal, Object project) {
		return (principal == null ? "{}":principal) + (project == null ? ("L[" + Project.name): ("R[" + project)); 
	}
}
