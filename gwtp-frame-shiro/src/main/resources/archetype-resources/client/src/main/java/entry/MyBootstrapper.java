package ${package}.entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

@Singleton
public class MyBootstrapper implements Bootstrapper {
    private final PlaceManager placeManager;
    private final CurrentUser user;

    @Inject
    public MyBootstrapper(
    		EventBus eventBus,
    		CurrentUser user,
            PlaceManager placeManager) {
    	this.user = user;
        this.placeManager = placeManager;

        eventBus.addHandler(UserRolesChangeEvent.getType(), new UserRolesChangeEvent.UserRolesChangeHandler() {

			@Override
			public void onUserStatusChange() {
				placeManager.revealCurrentPlace();				
			}
        	
        });
    }

    @Override
    public void onBootstrap() {
    	// TODO: ErrorPlace
    	user.sync(c->placeManager.revealCurrentPlace(), failure->GWT.log("sync account failure"));
    }
}