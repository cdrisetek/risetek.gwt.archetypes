package ${package}.presentermodules.projects.list;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	@UiField
	HTMLPanel dataDisplayPanel;

	@UiField
	MaterialSearch searchinput;

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@Inject
	public PageView(final EventBus eventBus, final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		searchinput.addKeyUpHandler(c -> {
			getUiHandlers().onSearch();
		});

		searchinput.addCloseHandler(event -> {
			searchinput.setValue(null);
			getUiHandlers().onSearch();
		});
	}

	@UiHandler("btnCreate")
	void onAddNewClick(ClickEvent e) {
		getUiHandlers().onCreateProjectPlace();
	}

	@Override
	public void alert(String message) {
		MaterialToast.fireToast(message);
	}

	@Override
	public String getSearchKey() {
		return searchinput.getValue();
	}

    @Override
    public void setInSlot(Object slot, IsWidget content) {
    	super.setInSlot(slot, content);
    	dataDisplayPanel.add(content);
    }
}
