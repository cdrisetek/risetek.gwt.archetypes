package ${package}.presentermodules.development.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.BaseHomeCardView;
import ${package}.utils.Icons;

public class HomeCardView extends BaseHomeCardView<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("开发者乐园", Icons.aboutIcon());
	}
}
