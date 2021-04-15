package ${package}.share.container;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StateEntity implements IsSerializable {
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(String message) {
		(this.message = Optional.ofNullable(this.message).orElse(new Vector<>())).add(message);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private String title;
	private List<String> message;
	private int type;
}
