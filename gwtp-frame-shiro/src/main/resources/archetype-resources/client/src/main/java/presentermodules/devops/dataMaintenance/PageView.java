package ${package}.presentermodules.devops.dataMaintenance;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<Widget, PageView> {}

	@Inject
	public PageView(final EventBus eventBus,
			        final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}
	
	@UiHandler("btnClearProjectCommit")
	public void onClearProjectClick(ClickEvent e) {
		getUiHandlers().onClearProject();
	}
	
	@UiHandler("btnRandomProjects")
	public void onRandomProjectsClick(ClickEvent e) {
		getUiHandlers().onRandomProject();
	}

	@UiHandler("btnClearAccountCommit")
	public void onClearAccountClick(ClickEvent e) {
		getUiHandlers().onClearAccount();
	}
	
	@UiHandler("btnRandomAccount")
	public void onRandomAccountClick(ClickEvent e) {
		getUiHandlers().onRandomAccount();
	}	
	
	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		getUiHandlers().onGoBackPlace();
	}
}
