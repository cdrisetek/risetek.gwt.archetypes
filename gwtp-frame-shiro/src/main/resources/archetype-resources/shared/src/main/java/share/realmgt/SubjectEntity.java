package ${package}.share.realmgt;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubjectEntity implements IsSerializable {
	public PrincipalEntity getPrincipal() {
		return principal;
	}
	public void setPrincipal(PrincipalEntity principal) {
		this.principal = principal;
	}
	public Set<RoleEntity> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@NotNull
	private PrincipalEntity principal;
	private Set<RoleEntity> roles;
}
