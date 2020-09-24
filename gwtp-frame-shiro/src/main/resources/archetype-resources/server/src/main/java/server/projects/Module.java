package ${package}.server.projects;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import ${package}.server.bindery.AutoLoadModule;
import ${package}.share.projects.ProjectAction;

@AutoLoadModule
public class Module extends HandlerModule {
	@Override
	protected void configureHandlers() {
		bind(IProjectsManagement.class).to(SimpleProjectsManagement.class).asEagerSingleton();
		bindHandler(ProjectAction.class, ProjectActionHandler.class);
	}
}
