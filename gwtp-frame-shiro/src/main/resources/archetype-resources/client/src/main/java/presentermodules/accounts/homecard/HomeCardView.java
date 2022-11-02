package ${package}.presentermodules.accounts.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.BaseHomeCardView;
import ${package}.utils.Icons;

public class HomeCardView extends BaseHomeCardView<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("账户管理", Icons.compassIcon());
	}
}
