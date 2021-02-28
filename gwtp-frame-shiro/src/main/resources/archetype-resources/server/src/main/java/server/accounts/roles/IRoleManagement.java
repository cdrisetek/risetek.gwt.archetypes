package ${package}.server.accounts.roles;

import java.util.Set;

public interface IRoleManagement {
	Set<String> getRoleSet(Object principal, Object project);
	// Replace roleSet with new one
	void setRoleSet(Object account, Object project, Set<String> roles);
}
