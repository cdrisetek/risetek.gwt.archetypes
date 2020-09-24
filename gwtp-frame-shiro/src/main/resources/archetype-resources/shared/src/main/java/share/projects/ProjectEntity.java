package ${package}.share.projects;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;
import ${package}.share.realmgt.RoleEntity;

public class ProjectEntity implements IsSerializable {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public RoleEntity getRoles() {
		return roles;
	}
	public void setRoles(RoleEntity roles) {
		this.roles = roles;
	}
	public Map<String, RoleEntity> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(Map<String, RoleEntity> userRoles) {
		this.userRoles = userRoles;
	}
	private int id;
	@NotNull @Size(max=64)
	private String name;
	@Size(max=130)
	private String note;
	private RoleEntity roles;
	private Map<String, RoleEntity> userRoles;
}
