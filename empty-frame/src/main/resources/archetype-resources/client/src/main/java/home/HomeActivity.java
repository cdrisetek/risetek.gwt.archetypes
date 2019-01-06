package ${package}.home;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import javax.inject.Inject;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {

	private final HomeView view;
	private final PlaceController placeController;

	@Inject
	HomeActivity(HomeViewImpl view, PlaceController placeController) {
		this((HomeView) view, placeController);
	}

	// For tests, independent from view implementation
	HomeActivity(HomeView view, PlaceController placeController) {
		this.view = view;
		this.placeController = placeController;
	}

	@Override
	public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);
	}

	@Override
	public void gotoPlace(Place palce) {
		placeController.goTo(palce);
	}
}
