package ${package}.share.accounts;

import com.google.gwt.user.client.rpc.IsSerializable;
import ${package}.share.accounts.roles.RoleEntity;

public class AuthorizationEntity implements IsSerializable {
	public RoleEntity getRole() {
		return role;
	}
	public void setRole(RoleEntity role) {
		this.role = role;
	}

	private RoleEntity role;
}
