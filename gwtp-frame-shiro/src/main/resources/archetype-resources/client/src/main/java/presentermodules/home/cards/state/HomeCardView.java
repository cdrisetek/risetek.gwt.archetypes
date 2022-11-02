package ${package}.presentermodules.home.cards.state;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.BaseHomeCardView;
import ${package}.utils.Icons;

public class HomeCardView extends BaseHomeCardView<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		setTitle("服务状态", Icons.serverIcon());
	}
}
