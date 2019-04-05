package ${package}.entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class LoggedInGatekeeper implements Gatekeeper {
	
	private final CurrentUser user;

	@Inject
	LoggedInGatekeeper(CurrentUser currentUser) {
		this.user = currentUser;
	}

	@Override
	public boolean canReveal() {
		return user.isLogin();
	}
}
