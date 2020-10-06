package ${package}.share.auth;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import ${package}.share.users.UserStateEntity;

public class UserEntity implements IsSerializable {
	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}
	public UserStateEntity getState() {
		return state;
	}
	public void setState(UserStateEntity state) {
		this.state = state;
	}
	private Map<String, String> descriptions;
	private UserStateEntity state;
}
