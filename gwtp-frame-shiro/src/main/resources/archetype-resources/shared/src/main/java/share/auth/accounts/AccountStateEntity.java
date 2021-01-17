package ${package}.share.auth.accounts;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AccountStateEntity implements IsSerializable {
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	boolean enable;
}
