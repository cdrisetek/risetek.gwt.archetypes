package ${package}.presentermodules.login;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWidget extends Composite {
	interface MyUiBinder extends UiBinder<HTMLPanel, LoginWidget> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private static final String buttonLogining = "\u767b\u5f55\u4e2d...";
	private static final String buttonLogin = "\u767b&nbsp;&nbsp;&nbsp;\u5f55";
	private static final String usernamePlaceholder = "\u8bf7\u8f93\u5165\u8d26\u53f7";
	private static final String passwordPlaceholder = "\u8bf7\u8f93\u5165\u5bc6\u7801";

	interface MyStyle extends CssResource {
		String box_tips_visible();
		String box_outer_border();
		String box_outer_border_highlight();
	}
	
	public interface LoginSubmitHandle {
		public void onSubmit(String username, String password, boolean rememberme);
		public void onNewAccount();
		public void onResetPassword();
	}
	
	@UiField MyStyle style;
	@UiField Button loginButton;
	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField CheckBox remembeme;
	@UiField SpanElement remembemeSpan;
	@UiField DivElement usernametip;
	@UiField DivElement passwordtip;
	@UiField Button newAccount;
	@UiField Button forget;
	@UiField Panel username_flowpanel;
	@UiField Panel password_flowpanel;
	
	public LoginWidget(boolean rememberMe, boolean hasRegister, LoginSubmitHandle submithandler) {
		initWidget(uiBinder.createAndBindUi(this));
		if(!rememberMe)
			remembemeSpan.getStyle().setDisplay(Display.NONE);

		if(!hasRegister)
			newAccount.getElement().getStyle().setDisplay(Display.NONE);

		username.getElement().setAttribute("placeholder", usernamePlaceholder);
		username.getElement().setAttribute("spellcheck", "false");
		username.getElement().setAttribute("autocapitalize", "off");
		username.getElement().setAttribute("autocomplete", "off");
		username.getElement().setAttribute("autocorrect", "off");
		password.getElement().setAttribute("placeholder", passwordPlaceholder);

		loginButton.addClickHandler(event->{
			boolean checkok = true;
			if (username.getValue().isEmpty()) {
				usernametip.addClassName(style.box_tips_visible());
				checkok = false;
			}
			if (password.getValue().isEmpty()) {
				passwordtip.addClassName(style.box_tips_visible());
				checkok = false;
			}
			if(!checkok)
				return;

			loginButton.setText(buttonLogining);
			submithandler.onSubmit(username.getValue(), password.getValue(), remembeme.getValue());
			username.setValue(null);
			password.setValue(null);
			username.setFocus(true);
		});

		newAccount.addClickHandler(event->{submithandler.onNewAccount();});
		forget.addClickHandler(event->{submithandler.onResetPassword();});
		
		username.addKeyPressHandler(event->{
			if(event.getCharCode() == KeyCodes.KEY_ENTER)
				loginButton.click();
			else
				reset();
		});

		username.addFocusHandler(event->{
			username_flowpanel.removeStyleName(style.box_outer_border());
			username_flowpanel.addStyleName(style.box_outer_border_highlight());
			reset();
		});

		username.addBlurHandler(event->{
			username_flowpanel.removeStyleName(style.box_outer_border_highlight());
			username_flowpanel.addStyleName(style.box_outer_border());
		});

		password.addKeyPressHandler(event->{
			if(event.getCharCode() == KeyCodes.KEY_ENTER)
				loginButton.click();
			else
				reset();
		});
		password.addBlurHandler(event->{
			password_flowpanel.removeStyleName(style.box_outer_border_highlight());
			password_flowpanel.addStyleName(style.box_outer_border());
		});

		password.addFocusHandler(event->{
			password_flowpanel.removeStyleName(style.box_outer_border());
			password_flowpanel.addStyleName(style.box_outer_border_highlight());
			reset();
		});
	}

	@Override
	protected void onLoad() {
		reset();
		password.setValue(null);
		username.setFocus(true);
	}

	public void setStatus(String status) {
		loginButton.setText(status);
	}

	private void reset() {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(buttonLogin);
		loginButton.setHTML(builder.toSafeHtml());
		passwordtip.removeClassName(style.box_tips_visible());
		usernametip.removeClassName(style.box_tips_visible());
	}
}
