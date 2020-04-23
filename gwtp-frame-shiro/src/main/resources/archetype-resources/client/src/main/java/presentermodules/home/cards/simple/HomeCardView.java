package ${package}.presentermodules.home.cards.simple;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		headLabel.setInnerHTML("应用信息");
		iconPanel.appendChild(Icons.compassIcon());
	}
}
