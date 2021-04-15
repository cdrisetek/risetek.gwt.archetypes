package ${package}.deployment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<Widget, PageView> {}

	@UiField Panel slot;
	@UiField MaterialCard panelAccount, panelDeploy;

	@Inject
	public PageView(final EventBus eventBus,
			        final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}

	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		getUiHandlers().onGoBackPlace();
	}

	@UiHandler("btnDeployCommit")
	public void onDeployCommitClick(ClickEvent e) {
		getUiHandlers().onTasksPlace();
	}

	@UiHandler("btnAccount")
	public void onAccountClick(ClickEvent e) {
		getUiHandlers().onAccountPlace();
	}
	
	@Override
	public void showAccount() {
		panelAccount.setVisible(true);
	}

	@Override
	public void showDeploy() {
		panelDeploy.setVisible(true);
	}
}
