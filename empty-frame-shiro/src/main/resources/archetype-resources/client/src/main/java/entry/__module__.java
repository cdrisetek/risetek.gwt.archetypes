package ${package}.entry;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import ${package}.MainActivityMapper;
import ${package}.login.UserRolesChangeEvent;

public class ${module} implements EntryPoint {

	public void onModuleLoad() {
		${module}Component component = Dagger${module}Component.create();

		SimplePanel mainContainer = new SimplePanel();
		MainActivityMapper mainMapper = component.mainActivityMapper();
		ActivityManager mainManager = new ActivityManager(mainMapper, component.eventBus());
		mainManager.setDisplay(mainContainer);

		RootPanel.get().add(mainContainer);

		// force current sync to server side.
		component.eventBus().addHandler(UserRolesChangeEvent.getType(), new UserRolesChangeEvent.UserRolesChangeHandler() {

			@Override
			public void onUserStatusChange() {
				component.placeHistoryHandler().handleCurrentHistory();
			}
			
		});
		component.currentUser().onAuthorityChanged();
	}
}
