package ${package}.server.database.hsqldb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import ${package}.server.devops.DevOpsTask;
import ${package}.server.devops.ServicesManagement;
import ${package}.share.container.StateEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;

class DatabaseManagement {
	static Logger logger = Logger.getLogger(DatabaseManagement.class.getName());

	private final String drvierClass = "org.hsqldb.jdbc.JDBCDriver";
	private final String dbName = "hsqldb";
//	private final String connectionURL = "jdbc:hsqldb:/.database/hsqldb/" + dbName + ";ifexists=false;sql.syntax_mys=true;shutdown=true";
	private final String connectionURL = "jdbc:hsqldb:mem:" + dbName + ";ifexists=false;sql.syntax_mys=true;shutdown=true";

	static class ColumnBase {
		String columnName;
		String columnDataType;
		String columnDefault = null;
		String foreignKey = null;
		long autoIncrementStart = 0;
		long autoIncrementInc = 0;
		boolean isPrimaryKey = false;

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(columnName);
			sb.append(" ");
			sb.append(columnDataType);
			return sb.toString();
		}
	}

	static class TableBase {
		String tableName;
		List<ColumnBase> columns = new Vector<>();

		String toCreateString() {
			StringBuffer sb = new StringBuffer();
			sb.append("CREATE TABLE IF NOT EXISTS " + tableName + "(");
			boolean isFirst = true;
			for (ColumnBase column : columns) {
				if (!isFirst)
					sb.append(", ");
				isFirst = false;
				/*
				sb.append(column.columnName);
				sb.append(" ");
				sb.append(column.columnDataType);
				*/
				sb.append(column.toString());
			}
			for (ColumnBase column : columns) {
				if(null == column.foreignKey)
					continue;
				sb.append(", FOREIGN KEY(").append(column.columnName).append(") REFERENCES ").append(column.foreignKey);
			}
			for (ColumnBase column : columns) {
				if (!column.isPrimaryKey)
					continue;
				sb.append(", PRIMARY KEY(");
				sb.append(column.columnName);
				sb.append(")");
			}

			sb.append(")");
			return sb.toString();
		}

		ColumnBase getColumn(String name) {
			for(ColumnBase column:columns) {
				if(column.columnName.equals(name))
					return column;
			}
			return null;
		}

		void tableTest(Connection connection) throws SQLException {
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			try (ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, new String[] { "TABLE" })) {
				if (!resultSet.next()) {
					try (Statement stmt = connection.createStatement()) {
						logger.info("Created table in given database..." + toCreateString());
						stmt.executeUpdate(toCreateString());
					} catch(SQLException e) {
						throw e;
					}
				}
			}
			logger.info(tableName + " TABLE should be: " + toCreateString());

			try(ResultSet resultColumnSet = databaseMetaData.getColumns(null, null, tableName, null)) {
				while(resultColumnSet.next()) {
					StringBuffer sb = new StringBuffer();
					String columnName = resultColumnSet.getString("COLUMN_NAME");
					sb.append(columnName).append(" ").append(resultColumnSet.getString("TYPE_NAME"));
					if(12 == resultColumnSet.getInt("DATA_TYPE"))
						sb.append("(" + resultColumnSet.getInt("COLUMN_SIZE") + ")");
					if(resultColumnSet.getInt("NULLABLE") == 0)
						sb.append(" NOT NULL");

					ColumnBase colum = getColumn(columnName);
					if(null != colum) {
						if(colum.toString().equals(sb.toString()))
							logger.info(tableName + " Column " + columnName + " matched: " + sb.toString());
						else {
							logger.info("Column dismatch: " + columnName);
							logger.info(colum.toString());
							logger.info(sb.toString());
							throw new SQLException("SQL mismatch: ");
						}
					}
				}
				// sb.append(" IS_AUTOINCREMENT: " + resultColumnSet.getString("IS_AUTOINCREMENT"));
//				sb.append(" " + resultColumnSet.getString("IS_NULLABLE"));
				// sb.append(" REMARKS: " + resultColumnSet.getString("REMARKS"));
				// System.out.println(sb.toString());
							
		/*					
							System.out.println("get columns");
							ResultSetMetaData rsmd = resultSet.getMetaData();
							for(int i = 1; i <= rsmd.getColumnCount(); i++)
								System.out.println("[" + i + "] " + rsmd.getColumnName(i) + " type:" + rsmd.getColumnTypeName(i)); 
							[1] TABLE_CAT type:VARCHAR
							[2] TABLE_SCHEM type:VARCHAR
							[3] TABLE_NAME type:VARCHAR
							[4] COLUMN_NAME type:VARCHAR
							[5] DATA_TYPE type:SMALLINT
							[6] TYPE_NAME type:VARCHAR
							[7] COLUMN_SIZE type:INTEGER
							[8] BUFFER_LENGTH type:INTEGER
							[9] DECIMAL_DIGITS type:INTEGER
							[10] NUM_PREC_RADIX type:INTEGER
							[11] NULLABLE type:INTEGER
							[12] REMARKS type:VARCHAR
							[13] COLUMN_DEF type:VARCHAR
							[14] SQL_DATA_TYPE type:INTEGER
							[15] SQL_DATETIME_SUB type:INTEGER
							[16] CHAR_OCTET_LENGTH type:INTEGER
							[17] ORDINAL_POSITION type:INTEGER
							[18] IS_NULLABLE type:VARCHAR
							[19] SCOPE_CATALOG type:VARCHAR
							[20] SCOPE_SCHEMA type:VARCHAR
							[21] SCOPE_TABLE type:VARCHAR
							[22] SOURCE_DATA_TYPE type:SMALLINT
							[23] IS_AUTOINCREMENT type:VARCHAR
							[24] IS_GENERATEDCOLUMN type:VARCHAR
		*/
							
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	private final void shutdown() {
		logger.severe("---------- shut down hsqldb database --------------------------");
		try {
			if(null != connection) {
				connection.close();
				connection = null;
				System.out.println("database connection closed");
			}
		} catch (SQLException e) {
			logger.severe("shut down hsqldb failed.");
		}
	}
	
	static void statisticsRead() {
		database.statistics_read_times++;
	}

	static void statisticsWrite() {
		database.statistics_upinsert_times++;
	}
	
	protected long statistics_read_times, statistics_upinsert_times, statistics_exception_times;

	public Connection getConnection() throws SQLException {
		if(null == connection)
			throw new SQLException("Connection unavaible");
		return connection;
	}
	
	private static class TableAccounts extends TableBase {
		public TableAccounts() {
			tableName = "ACCOUNTS";

			ColumnBase key = new ColumnBase();
			key.columnName = "NAME";
			key.columnDataType = "VARCHAR(64) NOT NULL";
			key.isPrimaryKey = true;
			columns.add(key);

			ColumnBase password = new ColumnBase();
			password.columnName = "PASSWORD";
			password.columnDataType = "OTHER NOT NULL";
			columns.add(password);

			ColumnBase descs = new ColumnBase();
			descs.columnName = "DESCRIPTIONS";
			descs.columnDataType = "OTHER";
			columns.add(descs);
		}
	}
	private final TableAccounts tableAccount = new TableAccounts();
	
	private static class TableProject extends TableBase {
		// "CREATE TABLE IF NOT EXISTS PROJECTS(NAME VARCHAR(128) NOT NULL, DESCRIPTIONS OTHER, PRIMARY KEY(NAME))";

		public TableProject() {
			tableName = "PROJECTS";

			ColumnBase key = new ColumnBase();
			key.columnName = "NAME";
			key.columnDataType = "VARCHAR(128) NOT NULL";
			key.isPrimaryKey = true;
			columns.add(key);

			ColumnBase descs = new ColumnBase();
			descs.columnName = "DESCRIPTIONS";
			descs.columnDataType = "OTHER";
			columns.add(descs);
		}
	}
	private final TableProject tableProject = new TableProject();

	private static class TableRole extends TableBase {
		// "CREATE TABLE IF NOT EXISTS ROLES(K VARCHAR(128) ARRAY NOT NULL, ROLES VARCHAR(16) ARRAY, PRIMARY KEY(K))";

		public TableRole() {
			tableName = "ROLES";

			ColumnBase key = new ColumnBase();
			key.columnName = "K";
			key.columnDataType = "VARCHAR(128) NOT NULL";
			key.isPrimaryKey = true;
//			key.foreignKey = "ACCOUNTS(NAME)";
			columns.add(key);

			ColumnBase roles = new ColumnBase();
			roles.columnName = "ROLES";
			roles.columnDataType = "VARCHAR(16) ARRAY";
			columns.add(roles);
		}
	}
	private final TableRole tableRole = new TableRole();

	public static final DatabaseManagement database = new DatabaseManagement();
	private Connection connection;
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

		ServicesManagement.addCleanerHandler(consumer -> shutdown());

		logger.info("Hsqldb database start up");
		DevOpsTask task = new DevOpsTask("Hsqldb Database Initialization", TaskType.FATAL, null);

		try {
			Class.forName(drvierClass);
		} catch (ClassNotFoundException e) {
			task.addMessage("Load JDBC driver failed: " + e.getMessage());
			task.stat = TaskState.FAILED;
			return;
		}

		// NOTE: unlock database.
		shutdown();

		try {
			connection = DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			shutdown();
			task.addMessage("Get connection failed: " + e.getMessage());
			task.stat = TaskState.FAILED;
			return;
		}

		try {
			tableAccount.tableTest(connection);
			task.addMessage("Account Table test passed");

			tableProject.tableTest(connection);
			task.addMessage("Projects Table test passed");

			tableRole.tableTest(connection);
			task.addMessage("Roles Table test passed");
		} catch (SQLException e) {
			task.addMessage("Failed on table test: " + e.getMessage());
			task.stat = TaskState.FAILED;
			return;
		}
		
		task.stat = TaskState.READY;
	}
}
