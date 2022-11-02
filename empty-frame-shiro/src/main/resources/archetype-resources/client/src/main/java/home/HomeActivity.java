package ${package}.home;

import javax.inject.Inject;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Prefix;
import ${package}.entry.ActivityPlace;
import ${package}.login.LoginUser;
import ${package}.login.UserRolesChangeEvent;
import ${package}.login.UserRolesChangeEvent.UserRolesChangeHandler;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter, UserRolesChangeHandler {

	@Prefix("Home")
	static public class HomePlace extends ActivityPlace<HomeActivity, HomePlace> {
	}

	private final HomeView view;
	private final PlaceController placeController;
	private final LoginUser loginUser;

	@Inject
	HomeActivity(HomeViewImpl view, PlaceController placeController, LoginUser loginUser) {
		this.view = view;
		this.placeController = placeController;
		this.loginUser = loginUser;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		eventBus.addHandler(UserRolesChangeEvent.getType(), this);
		onUserStatusChange();
		panel.setWidget(view);
	}

	@Override
	public void gotoPlace(Place palce) {
		placeController.goTo(palce);
	}

	@Override
	public void onUserStatusChange() {
		view.showLogin(!loginUser.isLogin());
	}

	@Override
	public void logout() {
		loginUser.Logout();
	}
}
