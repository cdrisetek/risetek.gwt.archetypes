package ${package}.home.cards.state;

import com.google.inject.Inject;
import ${package}.home.cards.InfoCard;
import ${package}.utils.Icons;

public class StateWidgetView extends InfoCard<MyUiHandlers> implements
		StateWidgetPresenter.MyView {

	@Inject
	public StateWidgetView() {
		headLabel.setInnerHTML("服务状态");
		iconPanel.appendChild(Icons.serverIcon());
	}
}
