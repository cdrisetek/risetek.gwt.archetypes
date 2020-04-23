package ${package}.presentermodules.realmgt.homecard;

import com.google.inject.Inject;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.presentermodules.realmgt.TokenNames;
import ${package}.utils.Icons;

public class HomeCardView extends InfoCard<MyUiHandlers> implements
		HomeCardPresenter.MyView {

	@Inject
	public HomeCardView() {
		headLabel.setInnerHTML("Realm management");
		iconPanel.appendChild(Icons.compassIcon());
		updateRedirect("Go to Realm management", "/#" + TokenNames.realmgt);
	}
}
