package ${package}.entry;

import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import ${package}.MainActivityMapper;
import ${package}.login.LoginUser;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ${module}Module.class)
public interface ${module}Component {

	EventBus eventBus();

	PlaceHistoryHandler placeHistoryHandler();

	MainActivityMapper mainActivityMapper();
	
	LoginUser currentUser();
	
	UnAuthRequestTransport unAuthAwareRequestTransport();
}
