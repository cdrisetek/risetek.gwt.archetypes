package ${package}.server.realmgt;

import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.share.GetResults;
import ${package}.share.realmgt.SubjectAction;
import ${package}.share.realmgt.AccountEntity;

public class SubjectActionHandler implements ActionHandler<SubjectAction, GetResults<AccountEntity>> {
	@Inject
	ISubjectManagement userManagement;
	
	@Override
	public GetResults<AccountEntity> execute(SubjectAction action, ExecutionContext context) throws ActionException {
		try{
		switch(action.op) {
		case CREATE:
			if(null == action.subjects)
				throw new ActionException("no valid datas");
			userManagement.CreateSubjects(action.subjects, action.password);
			break;
		case READ:
			if(null == action.subjects || action.subjects.size() == 0) {
				// READ all subjects
				List<AccountEntity> subjects = userManagement.ReadSubjects(action.like, action.offset, action.size);
				return new GetResults<AccountEntity>(subjects);
			}
			break;
		case UPDATE:
			userManagement.UpdateSubjects(action.subjects);
			break;
		case DELETE:
			break;
		case ENABLE:
			break;
		case DISABLE:
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
	public Class<SubjectAction> getActionType() {
		return SubjectAction.class;
	}

	@Override
	public void undo(SubjectAction action, GetResults<AccountEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
