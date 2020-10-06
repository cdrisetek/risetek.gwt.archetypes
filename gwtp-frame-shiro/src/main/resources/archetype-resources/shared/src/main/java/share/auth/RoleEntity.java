package ${package}.share.auth;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RoleEntity implements IsSerializable {
	public RoleEntity addRole(String roleName) {
		role.add(roleName);
		return this;
	}

	public RoleEntity addRole(Set<String> roles) {
		role.addAll(roles);
		return this;
	}

	public Set<String> getRole() {
		return role;
	}

	private Set<String> role = new HashSet<>();
}
