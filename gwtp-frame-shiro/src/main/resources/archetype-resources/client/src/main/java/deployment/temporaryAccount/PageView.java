package ${package}.deployment.temporaryAccount;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<Widget, PageView> {}

	@UiField Panel slot;

	@Inject
	public PageView(final EventBus eventBus,
			        final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}
	
	@UiHandler("btnAccountCommit")
	public void onAccountCommitClick(ClickEvent e) {
		getUiHandlers().onTemporaryAccount();
	}

	@Override
	public void showTask(String title, List<String> messages, String type, String state) {
		slot.add(new Label(title + " state: " + state));
	}
}
