package ${package}.entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class LoggedInGatekeeper implements Gatekeeper {
	
	private final Subject subject;

	@Inject
	LoggedInGatekeeper(Subject subject) {
		this.subject = subject;
	}

	@Override
	public boolean canReveal() {
		return subject.isLogin();
	}
}
