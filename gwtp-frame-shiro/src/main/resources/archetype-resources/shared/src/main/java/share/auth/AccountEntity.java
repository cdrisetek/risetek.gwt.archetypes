package ${package}.share.auth;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import ${package}.share.auth.accounts.AccountStateEntity;

public class AccountEntity implements IsSerializable {
	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}
	public AccountStateEntity getState() {
		return state;
	}
	public void setState(AccountStateEntity state) {
		this.state = state;
	}
	private Map<String, String> descriptions;
	private AccountStateEntity state;
}
