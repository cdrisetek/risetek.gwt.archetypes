package ${package}.share.realmgt;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountEntity implements IsSerializable {
	public AccountDescriptionsEntity getAccountDescriptions() {
		return accountDescriptions;
	}
	public void setAccountDescriptions(AccountDescriptionsEntity principal) {
		this.accountDescriptions = principal;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@NotNull
	private AccountDescriptionsEntity accountDescriptions;
	private Set<String> roles;
	private boolean enable;
	
	@NotNull // Account identity
	private String accountPrincipal;

	public String getAccountPrincipal() {
		return accountPrincipal;
	}
	public void setAccountPrincipal(String acountPrincipal) {
		this.accountPrincipal = acountPrincipal;
	}
}
