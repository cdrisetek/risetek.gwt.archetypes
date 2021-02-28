package ${package}.share.accounts;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountEntity implements IsSerializable {
	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDescription(EnumAccount attr, String value) {
		if(null == descriptions)
			descriptions = new HashMap<>();
		descriptions.put(attr.name(), value);
	}

	public String getDescription(EnumAccount attr) {
		if(null == descriptions)
			return null;
		return descriptions.get(attr.name());
	}

	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	private Map<String, String> descriptions;
	private String principal;
}
