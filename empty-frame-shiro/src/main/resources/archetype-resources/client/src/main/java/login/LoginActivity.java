package ${package}.login;

import javax.inject.Inject;
import java.util.function.Consumer;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import ${package}.entry.ActivityPlace;
import ${package}.home.HomeActivity.HomePlace;

public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	@Prefix("Login")
	static public class LoginPlace extends ActivityPlace<LoginActivity, LoginPlace> {
	}
	
	private final LoginView view;
	private final PlaceController placeController;
	private final LoginUser loginUser;

	@Inject
	LoginActivity(MyViewImpl view, PlaceController placeController, EventBus eventBus, LoginUser loginUser) {
		this.view = view;
		this.placeController = placeController;
		this.loginUser = loginUser;
	}

	@Override
	public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);
		eventBus.addHandler(UserRolesChangeEvent.getType(), new UserRolesChangeEvent.UserRolesChangeHandler() {
			@Override
			public void onUserStatusChange() {
				if(loginUser.isLogin())
					placeController.goTo(new HomePlace());
				else
					view.onFailed();
			}
		});
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Login(String username, String password, Consumer<ServerFailure> loginFailure) {
		loginUser.Login(username, password, loginFailure);
	}
}
