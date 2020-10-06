package ${package}.share.projects;

import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;
import ${package}.share.UniqueID;

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
	public Set<String> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<String> roleSet) {
		this.roleSet = roleSet;
	}
	public Map<UniqueID, Set<String>> getAuthorizations() {
		return roles;
	}
	public void setRoles(Map<UniqueID, Set<String>> roles) {
		this.roles = roles;
	}
	private int id;
	// Shoud be Unique
	@NotNull @Size(max=64)
	private String name;
	@Size(max=130)
	private String note;
	private Set<String> roleSet;
	private Map<UniqueID, Set<String>> roles;
}
