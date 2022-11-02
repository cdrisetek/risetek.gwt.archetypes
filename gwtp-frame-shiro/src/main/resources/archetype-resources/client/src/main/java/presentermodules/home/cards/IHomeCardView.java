package ${package}.presentermodules.home.cards;

import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtplatform.mvp.client.View;

public interface IHomeCardView extends View {
	void clear();
	void addAction(String title, ClickHandler handler);
	BaseHomeCardView<?> updateInfoItems(List<InfoItem> items);
}
