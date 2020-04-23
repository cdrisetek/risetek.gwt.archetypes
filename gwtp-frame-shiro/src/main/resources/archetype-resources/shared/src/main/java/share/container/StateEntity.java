package ${package}.share.container;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StateEntity implements IsSerializable {
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private String title;
	private String message;
	private int type;
}
