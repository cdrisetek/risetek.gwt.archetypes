package ${package}.server.auth.accounts;

import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.server.auth.IUserManagement;
import ${package}.share.GetResults;
import ${package}.share.auth.AccountEntity;
import ${package}.share.auth.accounts.AccountAction;

public class AccountActionHandler implements ActionHandler<AccountAction, GetResults<AccountEntity>> {
	@Inject
	IUserManagement userManagement;
	
	@Override
	public GetResults<AccountEntity> execute(AccountAction action, ExecutionContext context) throws ActionException {
		try{
		switch(action.op) {
		case READ:
			if(null == action.users || action.users.size() == 0) {
				// READ all subjects
				List<AccountEntity> subjects = userManagement.readUsers(null, action.like, action.offset, action.size);
				return new GetResults<AccountEntity>(subjects);
			}
			else
			{
				List<AccountEntity> subjects = userManagement.readUsers(action.users, action.like, action.offset, action.size);
				return new GetResults<AccountEntity>(subjects);
			}
		case UPSERT:
			userManagement.updateUsers(action.users);
			break;
		case DELETE:
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
	public Class<AccountAction> getActionType() {
		return AccountAction.class;
	}

	@Override
	public void undo(AccountAction action, GetResults<AccountEntity> result, ExecutionContext context)
			throws ActionException {
		// do nothing.
	}}
