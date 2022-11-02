package ${package}.login;

import java.util.function.Consumer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public interface LoginView extends IsWidget {

	public interface Presenter {
		public void onReset();
		public void Login(String username, String password, Consumer<ServerFailure> loginFailure);
	}

	public void onFailed();
	void setPresenter(Presenter presenter);
}
