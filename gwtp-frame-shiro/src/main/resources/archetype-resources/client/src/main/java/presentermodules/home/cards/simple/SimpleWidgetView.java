package ${package}.presentermodules.home.cards.simple;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class SimpleWidgetView extends InfoCard<MyUiHandlers> implements
		SimpleWidgetPresenter.MyView {

	@Inject
	public SimpleWidgetView() {
		headLabel.setInnerHTML("应用信息");
		iconPanel.appendChild(Icons.compassIcon());
	}
}
