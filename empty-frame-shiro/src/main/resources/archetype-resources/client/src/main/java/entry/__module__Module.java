package ${package}.entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryHandler.DefaultHistorian;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import ${package}.FreeFactory;
import ${package}.entry.UnAuthRequestTransport;
import ${package}.home.HomeActivity.HomePlace;
import ${package}.${module}PlaceHistoryMapper;
import ${package}.AppFactory;
import ${package}.CurrentUser;
import ${package}.IsAdmin;
import ${package}.LogoutUrl;
import ${package}.User;
import ${package}.login.LoginUser;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;

import javax.inject.Singleton;

@Module
public abstract class ${module}Module {

	private static Place getDefaultPlace() {
		return new HomePlace();
	}

	@Provides @Singleton
	static SimpleEventBus provideSimpleEventBus() {
		return new SimpleEventBus();
	}

	@Binds @Singleton
	abstract EventBus provideEventBus(SimpleEventBus bus);

	@Provides
	static PlaceHistoryMapper providePlaceHistoryMapper() {
		return GWT.create(${module}PlaceHistoryMapper.class);
	}

	@Provides
	static Historian provideHistorian() {
		// For best UX at login time (preserving place), use an Html5History: https://gist.github.com/1883821
		return new DefaultHistorian();
	}

	@Provides
	static UnAuthRequestTransport provideUnAuthAwareRequestTransport() {
		return new UnAuthRequestTransport();
	}

	@Provides
	static RequestTransport provideRequestTransport() {
		return new AuthAwareRequestTransport();
	}

	@Provides @Singleton
	static PlaceController providePlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	@Provides @Singleton
	static PlaceHistoryHandler providePlaceHistoryHandler(PlaceHistoryMapper mapper, Historian historian,
			PlaceController controller, EventBus eventBus) {
		PlaceHistoryHandler handler = new PlaceHistoryHandler(mapper, historian);
		handler.register(controller, eventBus, getDefaultPlace());
		return handler;
	}

	@Provides @Singleton
	static FreeFactory provideFreeFactory(EventBus eventBus, UnAuthRequestTransport transport) {
		FreeFactory factory = GWT.create(FreeFactory.class);
		factory.initialize(eventBus, transport);
		return factory;
	}

	@Provides @Singleton
	static ${module}Factory provide${module}Factory(EventBus eventBus, RequestTransport transport) {
		${module}Factory factory = GWT.create(${module}Factory.class);
		factory.initialize(eventBus, transport);
		return factory;
	}

	@Provides
	static Scheduler provideScheduler() {
		return Scheduler.get();
	}

	@Provides @LogoutUrl
	@JsProperty(name = "logoutUrl", namespace = JsPackage.GLOBAL)
	static native String provideLogoutUrl();

	@Provides @CurrentUser @Singleton
	@JsProperty(name = "user", namespace = JsPackage.GLOBAL)
	static native User provideCurrentUser();

	@Provides @CurrentUser
	static String provideUserName(@CurrentUser User user) {
		return user.getUserName();
	}

	@Provides @IsAdmin
	static boolean provideIsAdmin(@CurrentUser User user) {
		return user.isAdmin();
	}
	
	@Provides @Singleton
	static LoginUser provideLoginUser(FreeFactory freeFactory, EventBus eventBus) {
		LoginUser user = GWT.create(LoginUser.class);
		return user.initialize(freeFactory, eventBus);
	}
}
