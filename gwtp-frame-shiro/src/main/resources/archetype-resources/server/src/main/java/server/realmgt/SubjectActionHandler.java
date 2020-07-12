package ${package}.server.realmgt;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.share.GetResults;
import ${package}.share.realmgt.SubjectAction;
import ${package}.share.realmgt.SubjectEntity;

public class SubjectActionHandler implements ActionHandler<SubjectAction, GetResults<SubjectEntity>> {
	private static final Logger logger = Logger.getLogger(SubjectActionHandler.class.getName());

	@Inject
	ISubjectManagement userManagement;
	
	@Override
	public GetResults<SubjectEntity> execute(SubjectAction action, ExecutionContext context) throws ActionException {
		logger.fine("handler SubjectAction");
		try{
		switch(action.op) {
		case CREATE:
			if(null == action.subjects)
				throw new ActionException("no valid datas");
			userManagement.CreateSubjects(action.subjects);
			break;
		case READ:
			if(null == action.subjects || action.subjects.size() == 0) {
				// READ all subjects
				List<SubjectEntity> subjects = userManagement.ReadSubjects(action.like, action.offset, action.size);
				return new GetResults<SubjectEntity>(subjects);
			}
			break;
		case UPDATE:
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
	public void undo(SubjectAction action, GetResults<SubjectEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
