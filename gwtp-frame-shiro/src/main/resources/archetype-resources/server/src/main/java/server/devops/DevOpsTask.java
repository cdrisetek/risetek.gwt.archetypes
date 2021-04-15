package ${package}.server.devops;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import ${package}.share.devops.DevOpsTaskEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

public class DevOpsTask {
	static public List<DevOpsTask> tasks = new Vector<>();

	public interface TaskHandler {
		public void handler(DevOpsTask task);
	}

	private DevOpsTaskEntity entity = new DevOpsTaskEntity();
	
	public TaskState stat;
	public TaskType type;
	private TaskHandler handler;
	

	public DevOpsTask(String title, TaskType type, TaskHandler handler) {
		entity.setTitle(title);
		entity.setType(type);

		this.handler = handler;
		stat = TaskState.UNKNOW;
		tasks.add(this);
	}
	
	public void addMessage(String message) {
		entity.setMessage(message);
	}

	public TaskState getState() {
		if(null != handler)
			handler.handler(this);
		entity.setState(stat);
		return stat;
	}
	
	public void remove() {
		tasks.remove(this);
	}
	
	public DevOpsTaskEntity getEntity() {
		return entity;
	}
	
	static public List<DevOpsTaskEntity> getTasks() {
		return tasks.stream().map(t -> { t.getState(); return t.getEntity(); }).collect(Collectors.toList());
	}
}
