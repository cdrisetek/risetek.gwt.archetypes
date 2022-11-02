package ${package}.serverstatus;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import ${package}.${module}Factory;
import ${package}.entry.ActivityPlace;
import ${package}.home.HomeActivity.HomePlace;

public class ServerStatusActivity extends AbstractActivity implements ServerStatusView.Presenter {

	@Prefix("SStatus")
	static public class ServerStatusPlace extends ActivityPlace<ServerStatusActivity, ServerStatusPlace> {
	}

	private final ServerStatusView view;
	private final PlaceController placeController;
	private final AppFactory factory;

	@Inject
	ServerStatusActivity(MyViewImpl view, PlaceController placeController, AppFactory factory) {
		this.view = view;
		this.placeController = placeController;
		this.factory = factory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);
	}

	@Override
	public void updateStatus() {
		factory.serverstatus().statusServer().fire(
				new Receiver<List<ResponseProxy>>() {
					public void onFailure(ServerFailure failure) {
						GWT.log("failed");
					}

					@Override
					public void onSuccess(List<ResponseProxy> response) {
						for(ResponseProxy resp:response)
							view.appendStatus(resp.getTitle(), resp.getMessage());
					}
				});
	}
	
	@Override
	public void gotoHome() {
		placeController.goTo(new HomePlace());
	}
}
