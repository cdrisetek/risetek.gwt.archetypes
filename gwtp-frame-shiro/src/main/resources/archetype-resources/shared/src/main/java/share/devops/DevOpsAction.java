package ${package}.share.devops;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.dispatch.GetNoResult;

public class DevOpsAction extends UnsecuredActionImpl<GetNoResult> {

	public static enum OP implements IsSerializable {CLEAR_PROJECT, CLEAR_ACCOUNT};

	protected DevOpsAction(){}

	public DevOpsAction(OP op) {
		this.op = op;
	}
	public OP op;
}
