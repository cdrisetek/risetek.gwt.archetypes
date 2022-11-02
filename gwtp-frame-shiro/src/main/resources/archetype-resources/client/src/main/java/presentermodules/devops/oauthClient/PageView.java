package ${package}.presentermodules.devops.oauthClient;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	@UiField MaterialCard panelToken;
	@UiField MaterialLabel labelCode, labelState;

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

	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		getUiHandlers().onGoBackPlace();
	}

	@UiHandler("btnCommit")
	public void onCommitClick(ClickEvent e) {
		getUiHandlers().onOAuthClientRequest();
	}

	@UiHandler("btnTokenCommit")
	public void onTokenCommitClick(ClickEvent e) {
		getUiHandlers().onOAuthTokenRequest(code, state);
	}

	@Override
	public void tokenPanelSetVisible(boolean visible) {
		panelToken.setVisible(visible);
	}

	private String code, state;
	@Override
	public void setCode(String code) {
		this.code = code;
		labelCode.setText("获取：" + code);
	}

	@Override
	public void setState(String value) {
		state = value;
		labelState.setText("获取：" + state);
	}
}
