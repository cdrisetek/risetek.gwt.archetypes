package ${package}.share.users;

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.GetResults;
import ${package}.share.auth.UserEntity;

public class UserAction extends UnsecuredActionImpl<GetResults<UserEntity>> {

	public static enum OP implements IsSerializable { CREATE, READ, UPDATE, DELETE, ENABLE, DISABLE }; 	

	protected UserAction(){}

	// List User Entity
	public UserAction(int offset, int size, String like, long sequence) {
		users = null;
		password = null;
		op = OP.READ;
		this.offset = offset;
		this.size = size;
		this.like = like;
		this.sequence = sequence;
	}
	
	// Create a new User.
	public UserAction(Set<UserEntity> subjects, String password) {
		this.users = subjects;
		this.password = password;
		this.op = OP.CREATE;
		this.offset = 0;
		this.size = 0;
		this.like = null;
		this.sequence = 0;
	}

	// Update User descriptions.
	public UserAction(Set<UserEntity> subjects) {
		this.users = subjects;
		this.password = null;
		this.op = OP.UPDATE;
		this.offset = 0;
		this.size = 0;
		this.like = null;
		this.sequence = 0;
	}
	
	public UserAction(Set<UserEntity> subjects, String password, OP op, int offset, int size, String like, long sequence) {
		this.users = subjects;
		this.password = password;
		this.op = op;
		this.offset = offset;
		this.size = size;
		this.like = like;
		this.sequence = sequence;
	}
	
	public long sequence;
	public int offset;
	public int size;
	public String like;
	public OP op;
	public Set<UserEntity> users;
	public String password;
}
