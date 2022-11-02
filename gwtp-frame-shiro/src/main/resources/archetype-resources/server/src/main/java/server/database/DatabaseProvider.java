package ${package}.server.database;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.google.inject.Injector;
import ${package}.server.accounts.IAccountManagement;
import ${package}.server.accounts.IProjectsManagement;
import ${package}.server.accounts.IRoleManagement;
import ${package}.server.database.hsqldb.HsqldbAccountManagement;
import ${package}.server.database.hsqldb.HsqldbProjectsManagement;
import ${package}.server.database.hsqldb.HsqldbRoleManagement;
import ${package}.server.database.memory.SimpleAccountManagement;
import ${package}.server.database.memory.SimpleProjectsManagement;
import ${package}.server.database.memory.SimpleRoleManagement;

@Singleton
public class DatabaseProvider {
	
	private static boolean useHsql = true;
	
	static public class AccountManagement implements Provider<IAccountManagement> {
		@Inject
		Injector injector;

		@Override
		public IAccountManagement get() {
			if(useHsql)
				return injector.getInstance(HsqldbAccountManagement.class);
			else
				return injector.getInstance(SimpleAccountManagement.class);
		}
	}

	static public class RoleManagement implements Provider<IRoleManagement> {
		@Inject
		Injector injector;

		@Override
		public IRoleManagement get() {
			if(useHsql)
				return injector.getInstance(HsqldbRoleManagement.class);
			else
				return injector.getInstance(SimpleRoleManagement.class);
		}
	}

	static public class ProjectsManagement implements Provider<IProjectsManagement> {
		@Inject
		Injector injector;

		@Override
		public IProjectsManagement get() {
			if(useHsql)
				return injector.getInstance(HsqldbProjectsManagement.class);
			else
				return injector.getInstance(SimpleProjectsManagement.class);
		}
	}
}
