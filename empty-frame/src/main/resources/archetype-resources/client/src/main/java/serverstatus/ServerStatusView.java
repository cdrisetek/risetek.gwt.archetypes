package ${package}.serverstatus;

import com.google.gwt.user.client.ui.IsWidget;

public interface ServerStatusView extends IsWidget {

	public interface Presenter {
		public void gotoHome();
	}

	void setPresenter(Presenter presenter);
	void appendStatus(String title, String message);
}
