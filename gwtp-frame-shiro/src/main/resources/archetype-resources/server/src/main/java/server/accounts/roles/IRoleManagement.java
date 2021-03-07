package ${package}.server.accounts.roles;

import java.util.Set;

import ${package}.share.accounts.hosts.HostProjectRBAC;

public interface IRoleManagement {
	Set<String> getRoleSet(Object principal, Object project);
	// Replace roleSet with new one
	void setRoleSet(Object principal, Object project, Set<String> roles);

	// Host Roles
	Set<HostProjectRBAC> getRoleSet(Object principal);
	void setRoleSet(Object principal, Set<HostProjectRBAC> roles);
	String provider();
}
