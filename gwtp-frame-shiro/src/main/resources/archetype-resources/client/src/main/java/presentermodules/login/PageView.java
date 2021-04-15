package ${package}.presentermodules.login;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.SheetField;
import ${package}.utils.SheetField.TYPE;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.MaterialValueBox;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	private static final String buttonLogining = "登录中...";
	private static final String buttonLogin = "登录";

	@UiField MaterialValueBox<String> boxAccount, boxPassword;
	@UiField MaterialCheckBox remembeme;
	@UiField MaterialButton btnLogin;
	@UiField MaterialCard panelAccounEx;

	interface Binder extends UiBinder<Widget, PageView> {}
	private final SheetField fieldHeader;

	@Inject
    public PageView(Binder binder) {
    	initWidget(binder.createAndBindUi(this));
    	
    	boxAccount.setAutocomplete(false);
    	Element el = boxAccount.asValueBoxBase().getElement();
		el.setAttribute("spellcheck", "false");
		el.setAttribute("autocapitalize", "off");
		el.setAttribute("autocorrect", "off");
    	
    	boxPassword.setAutocomplete(false);
    	el = boxPassword.asValueBoxBase().getElement();
		el.setAttribute("spellcheck", "false");
		el.setAttribute("autocapitalize", "off");
		el.setAttribute("autocorrect", "off");
    	
    	// default no account operation panel.
    	// setAccountOperatorActive(false);
	
		// Build validation chain.
		(fieldHeader = new SheetField.Builder(boxAccount).type(TYPE.ACCOUNT).minLength(1).build())
		.nextField(boxPassword).type(TYPE.PASSWORD).minLength(1).setKeyHandler(event -> {
			if(event.getNativeKeyCode() != KeyCodes.KEY_ENTER)
				reset();
			else
				doLogin();
		}).build()
        .nextField(btnLogin).build();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		btnLogin.setText(buttonLogin);
		boxAccount.setFocus(true);
	}    

	@UiHandler("boxPassword")
	void onPasswordFocus(FocusEvent e) {
		fieldHeader.validate(boxPassword);
	}
	
	@UiHandler("btnLogin")
	void onLoginClick(ClickEvent e) {
		doLogin();
	}

	@UiHandler("newAccount")
	void onNewAccountClick(ClickEvent event) {
		getUiHandlers().newAccount();
	}
	
	private void reset() {
		btnLogin.setText(buttonLogin);
	}
	
	@Override
	public void setStatus(String status) {
		reset();
		MaterialToast.fireToast(status);
	}

	private void doLogin() {
		if(boxAccount.getValue().isEmpty() || boxPassword.getValue().isEmpty()) {
			fieldHeader.validate(btnLogin);
			return;
		}

		btnLogin.setText(buttonLogining);
		getUiHandlers().Login(boxAccount.getValue(), boxPassword.getValue(), remembeme.getValue());
		boxAccount.setValue(null);
		boxPassword.setValue(null);
		boxAccount.setFocus(true);
	}
}
