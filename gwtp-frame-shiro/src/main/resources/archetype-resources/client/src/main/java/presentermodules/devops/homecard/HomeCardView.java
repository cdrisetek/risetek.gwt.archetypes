package ${package}.presentermodules.devops.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.BaseHomeCardView;
import ${package}.utils.Icons;

public class HomeCardView extends BaseHomeCardView<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("DevOps", Icons.helpIcon());
	}
}
