package ${package}.home.cards.state;

import com.google.inject.Inject;
import ${package}.home.cards.InfoCard;
import ${package}.utils.Icons;

public class StateWidgetView extends InfoCard<MyUiHandlers> implements
		StateWidgetPresenter.MyView {

	@Inject
	public StateWidgetView() {
		label.setText("服务状态");
		iconPanel.getElement().appendChild(Icons.serverIcon());
		initWidget(getPanel());
	}
}
