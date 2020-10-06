package ${package}.presentermodules.login;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	private static final String buttonLogining = "登录中...";
	private static final String buttonLogin = "登录";

	@UiField
	MaterialTextBox username, password;
	
	@UiField
	MaterialCheckBox remembeme;

	@UiField
	MaterialButton loginButton;
	
	@UiField
	MaterialCard accountop;

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@Inject
    public PageView(Binder binder) {
    	initWidget(binder.createAndBindUi(this));
    	
    	username.setAutocomplete(false);
    	Element el = username.asValueBoxBase().getElement();
		el.setAttribute("spellcheck", "false");
		el.setAttribute("autocapitalize", "off");
		el.setAttribute("autocorrect", "off");
    	
    	password.setAutocomplete(false);
    	el = password.asValueBoxBase().getElement();
		el.setAttribute("spellcheck", "false");
		el.setAttribute("autocapitalize", "off");
		el.setAttribute("autocorrect", "off");
    	
    	// default no account operation panel.
    	setAccountOperatorActive(false);
    }

	@Override
	protected void onAttach() {
		super.onAttach();
		loginButton.setText(buttonLogin);
		username.setFocus(true);
	}    

	@UiHandler("username")
	void onUserNameFocus(FocusEvent e) {
		username.clearErrorText();
		password.clearErrorText();
	}

	@UiHandler("username")
	void onUserNameKeyEnter(KeyPressEvent event) {
		if(event.getCharCode() == KeyCodes.KEY_ENTER)
			doLogin();
		else
			reset();
	}
	
	@UiHandler("password")
	void onPasswordFocus(FocusEvent e) {
		username.clearErrorText();
		password.clearErrorText();
	}
	
	@UiHandler("password")
	void onPasswordKeyEnter(KeyPressEvent event) {
		if(event.getCharCode() == KeyCodes.KEY_ENTER)
			doLogin();
		else
			reset();
	}

	@UiHandler("loginButton")
	void onLoginClick(ClickEvent e) {
		doLogin();
	}

	@UiHandler("newAccount")
	void onNewAccountClick(ClickEvent event) {
		getUiHandlers().newAccount();
	}
	
	private void reset() {
		loginButton.setText(buttonLogin);
	}
	
	@Override
	public void setStatus(String status) {
		reset();
		MaterialToast.fireToast(status);
	}

	private void doLogin() {
		if(username.getValue().isEmpty())
			username.setErrorText("账户不能为空");
		if(password.getValue().isEmpty())
			password.setErrorText("密码不能为空");
		
		if(username.getValue().isEmpty() || password.getValue().isEmpty())
			return;

		loginButton.setText(buttonLogining);
		getUiHandlers().Login(username.getValue(), password.getValue(), remembeme.getValue(), "TODOProject");
		username.setValue(null);
		password.setValue(null);
		username.setFocus(true);
	}

	@Override
	public void setAccountOperatorActive(boolean enable) {
		accountop.setVisible(enable);
	}
}
