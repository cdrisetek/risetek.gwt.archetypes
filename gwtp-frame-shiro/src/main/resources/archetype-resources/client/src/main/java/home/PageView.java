package ${package}.home;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.home.cards.InfoCardContainer;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	private final StyleBundle.Style style = StyleBundle.resources.style();
	private final InfoCardContainer cardContainer;
	
	@Inject
	public PageView() {
		style.ensureInjected();
		SimplePanel container = new SimplePanel();
		container.setStyleName(style.homeStyle());
		initWidget(container);
		cardContainer = new InfoCardContainer().setParent(container).build();
	}
	
    @Override
    public void addToSlot(Object slot, IsWidget content) {
		if(PagePresenter.SLOT_CARD0 == slot) {
			cardContainer.addToColumn(content, 0);
		} else if(PagePresenter.SLOT_CARD1 == slot) {
			cardContainer.addToColumn(content, 1);
		} else if(PagePresenter.SLOT_CARD2 == slot) {
			cardContainer.addToColumn(content, 2);
		}
	}
}
