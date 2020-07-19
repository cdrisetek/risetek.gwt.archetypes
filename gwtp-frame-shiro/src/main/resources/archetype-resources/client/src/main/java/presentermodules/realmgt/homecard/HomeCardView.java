package ${package}.presentermodules.realmgt.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("Realm management", Icons.compassIcon());
		addAction("Go to Realm management", c->{getUiHandlers().reveal();});
	}
}
