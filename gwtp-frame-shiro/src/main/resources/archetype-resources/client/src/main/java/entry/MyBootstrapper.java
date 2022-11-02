package ${package}.entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

@Singleton
public class MyBootstrapper implements Bootstrapper {
    private final Subject subject;

    @Inject
    public MyBootstrapper(
    		final EventBus eventBus,
    		final Subject subject,
            final PlaceManager placeManager) {
    	this.subject = subject;
        eventBus.addHandler(SubjectChangeEvent.getType(), () -> {
        	try {
        		// NOTE: when reveal place is not permit, some exception as authentication
        		// fired, if not process this situation, browser fall in loop.
        		placeManager.revealCurrentPlace();
        	} catch(Exception e) {
            	placeManager.revealDefaultPlace();
        	};
        });
    }

    @Override
    public void onBootstrap() {
    	subject.accountSync();
    }
}