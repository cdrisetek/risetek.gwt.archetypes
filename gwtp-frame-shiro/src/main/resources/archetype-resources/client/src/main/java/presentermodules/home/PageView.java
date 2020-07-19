package ${package}.presentermodules.home;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.presentermodules.home.cards.InfoCardContainer;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	private final InfoCardContainer cardContainer;
	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@Inject
	public PageView(Binder uiBinder) {
		Panel container = uiBinder.createAndBindUi(this);
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
