package ${package}.server.accounts;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.share.accounts.projects.ProjectAction;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.dispatch.GetResults;
import ${package}.share.exception.ActionUnauthorizedException;

public class ProjectActionHandler implements ActionHandler<ProjectAction, GetResults<ProjectEntity>> {

	@Inject
	IProjectsManagement management;

	@Override
	public GetResults<ProjectEntity> execute(ProjectAction action, ExecutionContext context) throws ActionException {
		Subject subject = SecurityUtils.getSubject();
		try{
			switch(action.op) {
			case READ:
				if(!subject.isPermitted("projects:read"))
					throw new ActionUnauthorizedException("permission: read projects");
				
				List<ProjectEntity> projects = management.readProjects(action.projects, action.like, action.offset, action.limit);
				return new GetResults<ProjectEntity>(projects);
			case UPSERT:
				if(!subject.isPermitted("projects:create,update"))
					throw new ActionUnauthorizedException("permission: create or update projects");

				management.updateOrInsert(action.projects);
				break;
			default:
				break;
			}
		} catch(Throwable t) {
			ActionExceptionMapper.handler(t);
		}
		return null;
	}

	@Override
	public Class<ProjectAction> getActionType() {
		return ProjectAction.class;
	}

	@Override
	public void undo(ProjectAction action, GetResults<ProjectEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}

}
