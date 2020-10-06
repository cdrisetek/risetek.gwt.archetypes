package ${package}.presentermodules.security.password;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialTextBox;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@UiField Panel passwordMaintance;

	@UiField
	MaterialIcon passwordspy1open, passwordspy1close, passwordspy2open, passwordspy2close;

	@UiField
	FocusPanel passwordspy1, passwordspy2;

	@UiField
	MaterialTextBox password1, password2;

	@Inject
	public PageView(Binder uiBinder) {
		uiBinder.createAndBindUi(this);
		initWidget(passwordMaintance);
		passwordspy1.clear();
		passwordspy1.setTabIndex(-1);
		passwordspy1.add(passwordspy1open);
		passwordspy2.clear();
		passwordspy2.setTabIndex(-1);
		passwordspy2.add(passwordspy2open);
	}
	
	@Override
	protected void onAttach() {
		password1.setFocus(true);
	}

	@UiHandler("passwordtitle")
	void onPasswordTitleClick(ClickEvent event) {
		getUiHandlers().goContinue();
	}
	
	@UiHandler("passwordspy1")
	void onPasswordSpy1Click(ClickEvent event) {
		passwordspy1.clear();
		if (InputType.PASSWORD == password1.getType()) {
			password1.setType(InputType.TEXT);
			passwordspy1.add(passwordspy1close);
		} else {
			password1.setType(InputType.PASSWORD);
			passwordspy1.add(passwordspy1open);
		}
	}

	@UiHandler("passwordspy2")
	void onPasswordSpy2Click(ClickEvent event) {
		passwordspy2.clear();

		if (InputType.PASSWORD == password2.getType()) {
			password2.setType(InputType.TEXT);
			passwordspy2.add(passwordspy2close);
		} else {
			password2.setType(InputType.PASSWORD);
			passwordspy2.add(passwordspy2open);
		}
	}

	@UiHandler("passwordButton")
	void onPasswordbuttonClick(ClickEvent event) {
		if (!password1.getValue().isEmpty() && password1.getValue().equals(password2.getValue()))
			getUiHandlers().changePassword(password1.getValue());
		else
			password1.setErrorText("密码不一致。");
	}

	@UiHandler("password2")
	void onPassword2Focus(FocusEvent event) {
		GWT.log("password focus");
		password1.clearErrorText();
		password2.clearErrorText();
	}

	@UiHandler("password1")
	void onPassword1Focus(FocusEvent event) {
		GWT.log("password focus");
		password1.clearErrorText();
		password2.clearErrorText();
	}
}

