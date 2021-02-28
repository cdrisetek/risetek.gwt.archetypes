package ${package}.share.accounts.roles;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.dispatch.GetResult;

public class RoleAction extends UnsecuredActionImpl<GetResult<RoleEntity>> {
	protected RoleAction(){}

	// Read roleSet
	public RoleAction(String principal, String project) {
		this(principal, project, null);
	}

	// Update roleSet
	public RoleAction(String principal, String project, RoleEntity role) {
		this.principal = principal;
		this.project = project;
		this.roleEntity = role;
		this.sequence = sequence_inc++;
	}

	private static long sequence_inc = 0;
	public long sequence;
	public String principal;
	public String project;
	public RoleEntity roleEntity;
}
