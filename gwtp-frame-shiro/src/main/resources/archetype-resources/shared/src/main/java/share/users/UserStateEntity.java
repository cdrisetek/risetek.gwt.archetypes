package ${package}.share.users;

import com.google.gwt.user.client.rpc.IsSerializable;


public class UserStateEntity implements IsSerializable {
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	boolean enable;
}
