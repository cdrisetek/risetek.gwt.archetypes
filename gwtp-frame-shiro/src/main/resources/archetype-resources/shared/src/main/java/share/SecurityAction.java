package ${package}.share;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.realmgt.AccountEntity;

public class SecurityAction extends UnsecuredActionImpl<GetResult<AccountEntity>> {

	public static enum OP implements IsSerializable { LOGIN, LOGOUT, SYNC, UPDATE, NEWACCOUNT }; 	

	protected SecurityAction(){}

	
	public SecurityAction(AccountEntity subject, String password, boolean rememberme, OP op) {
		this.subject = subject;
		this.op = op;
		this.password = password;
		this.rememberme = rememberme;
	}
	public OP op;
	public AccountEntity subject;
	public String password;
	public boolean rememberme;
}
