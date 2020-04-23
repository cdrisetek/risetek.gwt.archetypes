package ${package}.presentermodules.home.cards.state;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		headLabel.setInnerHTML("服务状态");
		iconPanel.appendChild(Icons.serverIcon());
	}
}
