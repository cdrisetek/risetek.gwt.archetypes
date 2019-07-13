package ${package};

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class SecurityAction extends UnsecuredActionImpl<GetResult<SecurityInfo>> {

	public static enum OP implements IsSerializable { LOGIN, LOGOUT, SYNC, UPDATE, NEWACCOUNT }; 	

	protected SecurityAction(){}

	
	public SecurityAction(SecurityInfo securityInfo, OP op) {
		this.securityInfo = securityInfo;
		this.op = op;
	}
	public OP op;
	public SecurityInfo securityInfo;
}
