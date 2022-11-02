package ${package}.share.devops;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DevOpsTaskEntity implements IsSerializable {
	public static enum TaskState implements IsSerializable {UNKNOW, FAILED, READY}
	public static enum TaskType implements IsSerializable {AUTHOR /* without basic authorization */, FATAL, WARNING, INFO}
	
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(String message) {
		(this.message = Optional.ofNullable(this.message).orElse(new Vector<>())).add(message);
	}
	public TaskType getType() {
		return type;
	}
	public void setType(TaskType type) {
		this.type = type;
	}
	public TaskState getState() {
		return state;
	}
	public void setState(TaskState state) {
		this.state = state;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private String title;
	private List<String> message;
	private TaskType type;
	private TaskState state;
}
