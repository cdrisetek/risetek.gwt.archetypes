package ${package}.server.accounts;

import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.server.ActionExceptionMapper;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.dispatch.GetResults;

public class AccountActionHandler implements ActionHandler<AccountAction, GetResults<AccountEntity>> {
	@Inject
	IAccountManagement accountManagement;
	
	@Override
	public GetResults<AccountEntity> execute(AccountAction action, ExecutionContext context) throws ActionException {
		try{
		switch(action.op) {
		case READ:
			List<AccountEntity> accounts;
			if(null == action.accounts || action.accounts.size() == 0)
				// READ all subjects
				accounts = accountManagement.readAccounts(null, action.like, action.offset, action.size);
			else
				accounts = accountManagement.readAccounts(action.accounts, action.like, action.offset, action.size);

			return new GetResults<AccountEntity>(accounts);
		case UPSERT:
			if(null == action.password) {
				accountManagement.updateAccounts(action.accounts);
				break;
			}

			for(AccountEntity account:action.accounts)
				accountManagement.createAccount(account, action.password);
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
