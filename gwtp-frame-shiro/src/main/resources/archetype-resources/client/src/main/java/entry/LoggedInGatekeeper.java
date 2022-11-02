package ${package}.entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

@Singleton
public class LoggedInGatekeeper implements Gatekeeper {
	
	private final Subject subject;
    private final PlaceManager placeManager;

	@Inject
	LoggedInGatekeeper(Subject subject, PlaceManager placeManager) {
		this.subject = subject;
		this.placeManager = placeManager;
	}

	@Override
	public boolean canReveal() {
		boolean canreveal = subject.isLogin();
		// push PlaceRequest for continue reveal place when login.
		if(!canreveal)
			placeManager.updateHistory(placeManager.getCurrentPlaceRequest(), true);
		return canreveal;
	}
}
