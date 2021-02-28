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
    private final Subject subject;

    @Inject
    public MyBootstrapper(
    		EventBus eventBus,
    		Subject subject,
            PlaceManager placeManager) {
    	this.subject = subject;
        this.placeManager = placeManager;

        eventBus.addHandler(SubjectChangeEvent.getType(), new SubjectChangeEvent.SubjectChangeHandler() {

			@Override
			public void onSubjectChange() {
				placeManager.revealCurrentPlace();				
			}
        	
        });
    }

    @Override
    public void onBootstrap() {
    	subject.accountSync(c->placeManager.revealCurrentPlace(), 
    			failure->GWT.log("subject synchronzie failured on bootstrap"));
    }
}