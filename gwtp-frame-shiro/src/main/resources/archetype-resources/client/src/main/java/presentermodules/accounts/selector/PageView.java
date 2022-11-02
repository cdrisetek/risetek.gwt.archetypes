package ${package}.presentermodules.accounts.selector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.presenter.slots.Slot;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialSearch;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	interface Binder extends UiBinder<Widget, PageView> {}
	@UiField HTMLPanel slot;
    @UiField MaterialNavBar navBar, navBarSearch;
    @UiField MaterialSearch txtSearch;

    @Inject
	public PageView(final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		txtSearch.addOpenHandler(e -> {
			navBar.setVisible(false);
			navBarSearch.setVisible(true);
			txtSearch.getIconClose().setIconColor(Color.BLUE);
			txtSearch.getIconSearch().setIconColor(Color.BLUE);
		});
		txtSearch.addCloseHandler(e -> {
			navBar.setVisible(true);
			txtSearch.clear();
			getUiHandlers().onSearch();
			navBarSearch.setVisible(false);
		});
		txtSearch.addKeyUpHandler(e -> {
			getUiHandlers().onSearch();
		});
	}

    @Override
    public void bindSlot(Slot<PresenterWidget<?>> slot) {
    	bindSlot(slot, this.slot);
    }

    @UiHandler("lnkGoback")
	public void onGobackClick(ClickEvent e) {
		getUiHandlers().onGoBackPlace();
	}

    @UiHandler("btnSearch")
    public void onBtnSearchClick(ClickEvent e) {
    	txtSearch.open();
    }

	@Override
	public String getSearchKey() {
		return txtSearch.getValue();
	}
}
