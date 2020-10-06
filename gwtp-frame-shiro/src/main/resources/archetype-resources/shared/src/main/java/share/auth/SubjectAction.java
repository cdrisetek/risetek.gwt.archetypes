package ${package}.share.auth;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.GetResult;

/**
 * Action with users and password set to null stand for get current subject associated UserEntity.
 * Action with only users set to null stand for change password or reset password if password is empty.
 * Action with users and password stand for create/register new User.
 * 
 * @author wangyc@risetek.com
 *
 */
public class SubjectAction extends UnsecuredActionImpl<GetResult<UserEntity>> {
	// For get subject associated UserEntity
	public SubjectAction(){
		users = null;
		password = null;
	}

	// For change or reset subject associated user password
	public SubjectAction(String password){
		users = null;
		this.password = password;
	}
	
	// For change subject associated user descriptions.
	public SubjectAction(UserEntity users) {
		this.users = users;
		this.password = null;
	}

	// Create or register new user.
	public SubjectAction(UserEntity users, String password) {
		this.users = users;
		this.password = password;
	}
	
	public String password;
	public UserEntity users;
}
