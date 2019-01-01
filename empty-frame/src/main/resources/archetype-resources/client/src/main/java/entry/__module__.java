package ${package}.entry;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import ${package}.generator.IBuilderStamp;
import ${package}.entry.Dagger${module}Component;
import ${package}.MainActivityMapper;

public class ${module} implements EntryPoint {

	public void onModuleLoad() {
		${module}Component component = Dagger${module}Component.create();

		SimplePanel mainContainer = new SimplePanel();
		MainActivityMapper mainMapper = component.mainActivityMapper();
		ActivityManager mainManager = new ActivityManager(mainMapper, component.eventBus());
		mainManager.setDisplay(mainContainer);

		RootPanel.get().add(mainContainer);

		component.placeHistoryHandler().handleCurrentHistory();
	}
}
