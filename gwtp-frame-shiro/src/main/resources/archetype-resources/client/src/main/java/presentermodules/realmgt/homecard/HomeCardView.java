package ${package}.presentermodules.realmgt.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("账户管理", Icons.compassIcon());
		addAction("Go to Realm management", c->{getUiHandlers().reveal();});
	}
}
