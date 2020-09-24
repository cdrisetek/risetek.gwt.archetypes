package ${package}.share.realmgt;

import java.util.List;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RoleEntity implements IsSerializable {
	public RoleEntity() {
		role = new Vector<>();
	}

	public RoleEntity addRole(String roleName) {
		role.add(roleName);
		return this;
	}
	
	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}
	
	private List<String> role;
}
