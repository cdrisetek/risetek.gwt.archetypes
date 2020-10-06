package ${package}.server.users;

import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.server.auth.IUserManagement;
import ${package}.share.GetResults;
import ${package}.share.auth.UserEntity;
import ${package}.share.users.UserAction;

public class UserActionHandler implements ActionHandler<UserAction, GetResults<UserEntity>> {
	@Inject
	IUserManagement userManagement;
	
	@Override
	public GetResults<UserEntity> execute(UserAction action, ExecutionContext context) throws ActionException {
		try{
		switch(action.op) {
		/*
		case CREATE:
			if(null == action.subjects)
				throw new ActionException("no valid datas");
			userManagement.createUser(action.subjects, action.password);
			break;
		*/
		case READ:
			if(null == action.users || action.users.size() == 0) {
				// READ all subjects
				List<UserEntity> subjects = userManagement.readUsers(action.like, action.offset, action.size);
				return new GetResults<UserEntity>(subjects);
			}
			break;
		case UPDATE:
			userManagement.updateUsers(action.users);
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
	public Class<UserAction> getActionType() {
		return UserAction.class;
	}

	@Override
	public void undo(UserAction action, GetResults<UserEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
