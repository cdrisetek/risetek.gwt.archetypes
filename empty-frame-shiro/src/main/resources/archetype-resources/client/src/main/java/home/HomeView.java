package ${package}.home;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface HomeView extends IsWidget {

	public interface Presenter {
		public void gotoPlace(Place place);
		public void logout();
	}

	void setPresenter(Presenter presenter);
	void showLogin(boolean on);
}
