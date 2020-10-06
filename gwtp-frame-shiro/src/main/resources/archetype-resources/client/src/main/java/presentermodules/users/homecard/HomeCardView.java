package ${package}.presentermodules.users.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("用户管理", Icons.compassIcon());
		addAction("Go to User management", c->{getUiHandlers().reveal();});
	}
}
