package ${package};

import java.util.Map;

import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class UpdateSecurityAction extends ActionImpl<GetNoResult> {
	protected UpdateSecurityAction(){}
	public UpdateSecurityAction(Map<String, String> security) {
		this.security = security;
	}

	public Map<String, String> security;
}
