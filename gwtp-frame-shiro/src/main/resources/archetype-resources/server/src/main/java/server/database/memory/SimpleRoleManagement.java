package ${package}.server.database.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import ${package}.server.accounts.IRoleManagement;
import ${package}.server.devops.ServicesManagement;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.container.StateEntity;
import ${package}.share.templates.Project;

@Singleton
public class SimpleRoleManagement implements IRoleManagement {
	
	public SimpleRoleManagement() {
		ServicesManagement.provideState(() -> {
			StateEntity state = new StateEntity();
			state.setTitle("Roles Management");
			state.setMessage("Memory based SimpleRoleManagement");
			state.setType(3);
			return state;
		});

	}
	
	@Override
	public Set<String> getRoleSet(Object principal, Object project) {
		Object key = makeKey(principal, project);
		return Optional.ofNullable(roleMap.get(key)).orElse(new HashSet<>());
	}

	@Override
	public void setRoleSet(Object principal, Object project, Set<String> roles) {
		Object key = makeKey(principal, project);
		roleMap.put(key, roles);
	}

	@Override
	public void setRoleSet(Object principal, Set<HostProjectRBAC> roles) {
		Object key = makeKey(principal, null);
		roleMap.put(key, roles.stream().map(r -> r.name()).collect(Collectors.toSet()));
	}

	@Override
	public Set<HostProjectRBAC> getRoleSet(Object principal) {
		Set<String> roles = getRoleSet(principal, null);
		return roles.stream().map(r -> HostProjectRBAC.valueOf(r)).collect(Collectors.toSet());
	}

	private Map<Object, Set<String>> roleMap = new HashMap<>();
	
	private String makeKey(Object principal, Object project) {
		return (principal == null ? "{}":principal) + (project == null ? ("L[" + Project.name): ("R[" + project)); 
	}
}
