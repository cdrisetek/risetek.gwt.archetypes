package ${package}.home;

import com.google.gwt.user.client.ui.IsWidget;

public interface HomeView extends IsWidget {

	public interface Presenter {
	}

	void setPresenter(Presenter presenter);
}
