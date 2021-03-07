package ${package}.share.accounts.hosts;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HostProjectEntity implements IsSerializable {

	public String[] getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String[] descriptions) {
		this.descriptions = descriptions;
	}

	private String[] descriptions;
}
