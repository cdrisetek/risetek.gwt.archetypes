package ${package}.share.accounts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.dispatch.GetResults;

public class AccountAction extends UnsecuredActionImpl<GetResults<AccountEntity>> {

	public static enum OP implements IsSerializable {READ, UPSERT};

	protected AccountAction(){}

	// List User Entity
	public AccountAction(Set<AccountEntity> accounts, int offset, int size, String like) {
		this.accounts = accounts;
		password = null;
		op = OP.READ;
		this.offset = offset;
		this.size = size;
		this.like = like;
		sequence = sequence_inc++;
	}

	// Create a new User.
	public AccountAction(String password, Set<AccountEntity> accounts) {
		this.accounts = accounts;
		this.password = password;
		this.op = OP.UPSERT;
		this.offset = 0;
		this.size = 0;
		this.like = null;
		this.sequence = sequence_inc++;
	}

	// Create a new User.
	public AccountAction(String password, AccountEntity...accounts) {
		this(password, new HashSet<>(Arrays.asList(accounts)));
	}
	
	// Update User descriptions.
	public AccountAction(Set<AccountEntity> accounts) {
		this(null, accounts);
	}
	
	// Update User descriptions.
	public AccountAction(AccountEntity...accounts) {
		this(null, new HashSet<>(Arrays.asList(accounts)));
	}
	
	private static long sequence_inc = 0;
	public long sequence;
	public int offset;
	public int size;
	public String like;
	public OP op;
	public Set<AccountEntity> accounts;
	public String password;
}
