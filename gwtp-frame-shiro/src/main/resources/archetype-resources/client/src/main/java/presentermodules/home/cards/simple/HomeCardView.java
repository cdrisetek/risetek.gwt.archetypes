package ${package}.presentermodules.home.cards.simple;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.BaseHomeCardView;
import ${package}.utils.Icons;

public class HomeCardView extends BaseHomeCardView<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("应用信息", Icons.compassIcon());
	}
}
