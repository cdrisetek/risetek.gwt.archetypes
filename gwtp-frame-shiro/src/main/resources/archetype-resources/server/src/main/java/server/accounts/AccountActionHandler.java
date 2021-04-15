package ${package}.server.accounts;

import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.dispatch.GetResults;
import ${package}.share.exception.ActionUnauthorizedException;

public class AccountActionHandler implements ActionHandler<AccountAction, GetResults<AccountEntity>> {
	@Inject
	IAccountManagement accountManagement;
	@Inject
	TemporaryAccount temporaryAccount;
	
	@Override
	public GetResults<AccountEntity> execute(AccountAction action, ExecutionContext context) throws ActionException {
		Subject subject = SecurityUtils.getSubject();
		switch(action.op) {
		case READ:
			if(!subject.isPermitted("accounts:read"))
				throw new ActionUnauthorizedException("permission: read accounts");
			List<AccountEntity> accounts;
			if(null == action.accounts || action.accounts.size() == 0)
				// READ all subjects
				accounts = accountManagement.readAccounts(null, action.like, action.offset, action.size);
			else
				accounts = accountManagement.readAccounts(action.accounts, action.like, action.offset, action.size);

			return new GetResults<AccountEntity>(accounts);
		case UPSERT:
			if(null == action.password) {
				if(!subject.isPermitted("accounts:update"))
					throw new ActionUnauthorizedException("permission: update accounts");

				accountManagement.updateAccounts(action.accounts);
				break;
			} else if(null == action.accounts || action.accounts.isEmpty()) {
				temporaryAccount.createTemporaryAccount();
				break;
			}

			if(!subject.isPermitted("accounts:create"))
				throw new ActionUnauthorizedException("permission: create accounts");
			for(AccountEntity account:action.accounts)
				accountManagement.createAccount(account, action.password);
			break;
		default:
			break;
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
