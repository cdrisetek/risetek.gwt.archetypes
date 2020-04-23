package ${package}.realmgt;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PrincipalEntity implements IsSerializable {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	
	@NotNull
	@Size(max=20)
	private String name;
	
	@Size(max=50)
	private String email;
	
	@Size(max=20)
	private String telphone;
}
