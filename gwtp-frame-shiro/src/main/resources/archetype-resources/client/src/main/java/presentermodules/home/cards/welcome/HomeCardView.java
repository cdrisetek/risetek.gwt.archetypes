package ${package}.presentermodules.home.cards.welcome;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		headLabel.setInnerHTML("欢迎使用");
		iconPanel.appendChild(new Icons.Login().getElement());
	}
}
