package ${package}.presentermodules.auth.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("项目管理", Icons.compassIcon());
		addAction("Go to Project management", c->{getUiHandlers().reveal();});
	}
}
