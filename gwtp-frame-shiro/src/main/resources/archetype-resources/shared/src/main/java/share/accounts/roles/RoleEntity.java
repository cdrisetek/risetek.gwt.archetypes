package ${package}.share.accounts.roles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RoleEntity implements IsSerializable {
	public RoleEntity addRoleSet(Set<String> roles) {
		Optional.ofNullable(roles).ifPresent(r -> roleSet.addAll(r));
		return this;
	}

	public Set<String> getRoleSet() {
		return roleSet;
	}

	private Set<String> roleSet = new HashSet<>();
}
