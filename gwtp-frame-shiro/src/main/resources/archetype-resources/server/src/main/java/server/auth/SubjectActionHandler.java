package ${package}.server.auth;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.share.GetResult;
import ${package}.share.auth.SubjectAction;
import ${package}.share.auth.UserEntity;

public class SubjectActionHandler implements ActionHandler<SubjectAction, GetResult<UserEntity>> {
	@Inject
	IUserManagement userManagement;
	
	@Override
	public GetResult<UserEntity> execute(SubjectAction action, ExecutionContext context) throws ActionException {
		UserEntity userEntity = null;

		// Get current subject associated user entity.
		if(null == action.users && null == action.password) {
			try {
				userEntity = userManagement.getSubjectUserEntity();
			} catch (Exception e) {
				ActionExceptionMapper.handler(e);
			}
		} 
		
		// Change or reset current subject associated user password.
		// If action.password is not null but is empty means reset password.
		else if(null == action.users && null != action.password) {
			try {
				userManagement.setSubjectUserPassword(action.password);
			} catch (Exception e) {
				// This will throw a ActionUnauthenticatedException which is serializable
				// to Client and handler by client.
				ActionExceptionMapper.handler(e);
			}
		}

		// Register new user.
		// TODO: this function should be UserAction?
		else if(null != action.users && null != action.password) {
			System.out.println("TODO: Register new user.");
		}

		// Update subject associated user descriptions.
		else if(null != action.users) {
			System.out.println("TODO: Update subject associated user descriptions.");
		}

		return new GetResult<>(userEntity);
	}

	@Override
	public Class<SubjectAction> getActionType() {
		return SubjectAction.class;
	}

	@Override
	public void undo(SubjectAction action, GetResult<UserEntity> result, ExecutionContext context) throws ActionException {}
}
