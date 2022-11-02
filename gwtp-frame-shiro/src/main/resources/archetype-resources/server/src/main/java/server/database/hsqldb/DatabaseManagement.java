package ${package}.server.database.hsqldb;

import java.util.logging.Logger;

import ${package}.server.devops.DevOpsTask;
import ${package}.server.devops.ServicesManagement;
import ${package}.share.container.StateEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

class DatabaseManagement {
	static Logger logger = Logger.getLogger(DatabaseManagement.class.getName());
	
	static void statisticsRead() {
		database.statistics_read_times++;
	}

	static void statisticsWrite() {
		database.statistics_upinsert_times++;
	}
	
	protected long statistics_read_times, statistics_upinsert_times, statistics_exception_times;
	
	public static final DatabaseManagement database = new DatabaseManagement();
	private DatabaseManagement() {
		ServicesManagement.provideState(() -> {
			StateEntity state = new StateEntity();
			state.setTitle("HSQLDB Database");
			state.setMessage("Read: " + statistics_read_times + " times");
			state.setMessage("Write:" + statistics_upinsert_times + " times");
			state.setMessage("Error:" + statistics_exception_times + " times");
			state.setType(3);
			return state;
		});

		logger.info("Hsqldb database start up");
		DevOpsTask task = new DevOpsTask("Hsqldb Database Initialization", TaskType.FATAL, null);

		task.stat = TaskState.READY;
	}
}
