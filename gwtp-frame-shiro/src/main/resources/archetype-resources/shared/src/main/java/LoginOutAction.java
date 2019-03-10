package ${package};

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class LoginOutAction extends UnsecuredActionImpl<GetResult<AuthorityInfo>> {
	protected LoginOutAction(){}
	public LoginOutAction(AuthToken authToken) {
		this.authToken = authToken;
	}
	public AuthToken authToken;
}
