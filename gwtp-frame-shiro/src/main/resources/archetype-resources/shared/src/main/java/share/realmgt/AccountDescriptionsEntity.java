package ${package}.share.realmgt;

import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountDescriptionsEntity implements IsSerializable {

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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Size(max=50)
	private String email;
	
	@Size(max=20)
	private String telphone;

	@Size(max=200)
	private String notes;
}
