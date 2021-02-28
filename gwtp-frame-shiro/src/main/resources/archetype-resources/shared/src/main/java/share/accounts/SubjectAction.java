package ${package}.share.accounts;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.dispatch.GetResult;

/**
 * Action with users and password set to null stand for get current subject associated AccountEntity.
 * Action with only users set to null stand for change password or reset password if password is empty.
 * Action with users and password stand for create/register new User.
 * 
 * @author wangyc@risetek.com
 *
 */
public class SubjectAction extends UnsecuredActionImpl<GetResult<AccountEntity>> {
	// For get subject associated AccountEntity
	public SubjectAction() {
		this(null, null);
	}

	// For change or reset subject associated user password
	public SubjectAction(String password) {
		this(null, password);
	}
	
	// For change subject associated user descriptions.
	public SubjectAction(AccountEntity account) {
		this(account, null);
	}

	// Create or register new user.
	public SubjectAction(AccountEntity account, String password) {
		this.account = account;
		this.password = password;
	}
	
	public String password;
	public AccountEntity account;
}
