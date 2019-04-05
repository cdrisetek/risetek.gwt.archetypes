package ${package}.home.cards.welcome;

import com.google.inject.Inject;
import ${package}.home.cards.InfoCard;
import ${package}.utils.Icons;

public class WelcomeWidgetView extends InfoCard<MyUiHandlers> implements
		WelcomeWidgetPresenter.MyView {

	@Inject
	public WelcomeWidgetView() {
		headLabel.setText("欢迎使用");
		iconPanel.getElement().appendChild(Icons.loginIcon());
		initWidget(getPanel());
	}
}
