package ${package}.login;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWidget extends SimplePanel {
	private final StyleBundle.Style style = StyleBundle.resources.style();

	private final String loginTitle = "\u5bc6\u7801\u767b\u5f55";
	private final String usernamePlaceholder = "\u8bf7\u8f93\u5165\u8d26\u53f7";
	private final String passwordPlaceholder = "\u8bf7\u8f93\u5165\u5bc6\u7801";
	private final String buttonLogin = "\u767b&nbsp;&nbsp;&nbsp;\u5f55";
	private final String buttonLogining = "\u767b\u5f55\u4e2d...";
	private final String passwordTipsText = "\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
	private final String usernameTipsText = "\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a";
	private final CheckBox rememberBox = new CheckBox("\u81ea\u52a8\u767b\u5f55");
	private final Button forget = new Button("\u5fd8\u8bb0\u5bc6\u7801");

	public interface LoginSubmitHandle {
		public void onSubmit(String username, String password, boolean rememberme);
	}

	private final TextBox usernameInputBox = new TextBox();
	private final PasswordTextBox passwordInputBox = new PasswordTextBox();
	private final SimplePanel passwordTipsPanel = new SimplePanel();
	private final SimplePanel usernameTipsPanel = new SimplePanel();
	
	private final Button loginSubmit = new Button();

	public LoginWidget(boolean rememberMe, LoginSubmitHandle submithandler) {
		style.ensureInjected();
		setStyleName(style.loginWidget());

		loginSubmit.addClickHandler(event->{
			boolean checkok = true;
			if (usernameInputBox.getValue().isEmpty()) {
				usernameTipsPanel.setStyleName(style.box_tips_visible(), true);
				checkok = false;
			}
			if (passwordInputBox.getValue().isEmpty()) {
				passwordTipsPanel.setStyleName(style.box_tips_visible(), true);
				checkok = false;
			}
			if(!checkok)
				return;

			loginSubmit.setText(buttonLogining);
			submithandler.onSubmit(usernameInputBox.getValue(), passwordInputBox.getValue(), rememberBox.getValue());
			usernameInputBox.setValue("");
		});

		FlowPanel backgroundPanel = new FlowPanel();
		backgroundPanel.setStyleName(style.background());

		SimplePanel title = new SimplePanel();
		Element div = DOM.createDiv();
		div.setInnerHTML(loginTitle);
		title.getElement().setInnerHTML(div.getString());
		title.setStyleName(style.topTitle());
		backgroundPanel.add(title);
		addLineTo(backgroundPanel);

		FlowPanel interactiveContainer = new FlowPanel();
		interactiveContainer.setStyleName(style.interactiveContainer());
		backgroundPanel.add(interactiveContainer);
		
		// --- USERNAME BOX -----
		FlowPanel username_flowpanel = new FlowPanel();
		username_flowpanel.setStyleName(style.box_outer());
		username_flowpanel.addStyleName(style.box_outer_border());
		SimplePanel username_icon = new SimplePanel();
		username_icon.setStyleName(style.box_icon());
		Image user_img = new Image(StyleBundle.resources.user_png());
		user_img.setStyleName(style.box_icon_img());
		username_icon.add(user_img);
		username_flowpanel.add(username_icon);
		SimplePanel username_input_panel = new SimplePanel();
		username_flowpanel.add(username_input_panel);
		username_input_panel.setStyleName(style.box_input());
		username_input_panel.add(usernameInputBox);

		username_flowpanel.add(usernameTipsPanel);
		usernameTipsPanel.setStyleName(style.box_tips());
		usernameTipsPanel.getElement().setInnerText(usernameTipsText);
		div = DOM.createDiv();
		div.setInnerHTML(usernameTipsText);
		usernameTipsPanel.getElement().setInnerHTML(div.getString());

		usernameInputBox.addKeyPressHandler(event->{
			if(event.getCharCode() == KeyCodes.KEY_ENTER)
				loginSubmit.click();
			else
				reset();
		});

		usernameInputBox.addFocusHandler(event->{
			username_flowpanel.removeStyleName(style.box_outer_border());
			username_flowpanel.addStyleName(style.box_outer_border_highlight());
			reset();
		});

		usernameInputBox.addBlurHandler(event->{
			username_flowpanel.removeStyleName(style.box_outer_border_highlight());
			username_flowpanel.addStyleName(style.box_outer_border());
		});

		usernameInputBox.setStyleName(style.box_input_area());
		interactiveContainer.add(username_flowpanel);

		usernameInputBox.getElement().setAttribute("placeholder", usernamePlaceholder);
		usernameInputBox.getElement().setAttribute("spellcheck", "false");
		usernameInputBox.getElement().setAttribute("autocapitalize", "off");
		usernameInputBox.getElement().setAttribute("autocomplete", "off");
		usernameInputBox.getElement().setAttribute("autocorrect", "off");

		// Interval 1
		addIntervalTo(interactiveContainer);

		// --- PASSWORD BOX ----
		FlowPanel password_flowpanel = new FlowPanel();
		password_flowpanel.setStyleName(style.box_outer());
		password_flowpanel.addStyleName(style.box_outer_border());

		SimplePanel password_icon = new SimplePanel();
		password_icon.setStyleName(style.box_icon());
		Image password_img = new Image(StyleBundle.resources.password_png());
		password_img.setStyleName(style.box_icon_img());
		password_icon.add(password_img);
		password_flowpanel.add(password_icon);
		SimplePanel password_input_panel = new SimplePanel();
		password_flowpanel.add(password_input_panel);
		password_input_panel.setStyleName(style.box_input());
		password_input_panel.add(passwordInputBox);

		password_flowpanel.add(passwordTipsPanel);
		passwordTipsPanel.setStyleName(style.box_tips());
		passwordTipsPanel.getElement().setInnerText(passwordTipsText);

		passwordInputBox.addKeyPressHandler(event->{
			if(event.getCharCode() == KeyCodes.KEY_ENTER)
				loginSubmit.click();
			else
				reset();
		});
		passwordInputBox.addBlurHandler(event->{
			password_flowpanel.removeStyleName(style.box_outer_border_highlight());
			password_flowpanel.addStyleName(style.box_outer_border());
		});

		passwordInputBox.addFocusHandler(event->{
			password_flowpanel.removeStyleName(style.box_outer_border());
			password_flowpanel.addStyleName(style.box_outer_border_highlight());
			reset();
		});
		
		passwordInputBox.setStyleName(style.box_input_area());
		passwordInputBox.getElement().setAttribute("placeholder", passwordPlaceholder);
		interactiveContainer.add(password_flowpanel);

		// Interval 2
		addIntervalTo(interactiveContainer);

		loginSubmit.setStyleName(style.loginButton());
		interactiveContainer.add(loginSubmit);

		// footer
		FlowPanel footer = new FlowPanel();
		footer.setStyleName(style.footer());
		interactiveContainer.add(footer);

		rememberBox.setValue(true);
		if(rememberMe) {
			rememberBox.setStyleName(style.footerItem());
			footer.add(rememberBox);
		}
		
		forget.setStyleName(style.footerItem());
		forget.setStyleName(style.forget(), true);
		footer.add(forget);

		add(backgroundPanel);
	}

	@Override
	protected void onLoad() {
		reset();
		passwordInputBox.setText(null);
		usernameInputBox.setFocus(true);
	}

	public void setStatus(String status) {
		loginSubmit.setText(status);
	}

	private void reset() {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(buttonLogin);
		loginSubmit.setHTML(builder.toSafeHtml());
		passwordTipsPanel.removeStyleName(style.box_tips_visible());
		usernameTipsPanel.removeStyleName(style.box_tips_visible());
	}
	
	private void addLineTo(Panel parent) {
		Element div = DOM.createDiv();
		div.setClassName(style.line());
		parent.getElement().appendChild(div);
	}

	private void addIntervalTo(Panel parent) {
		Element div = DOM.createDiv();
		div.setClassName(style.interval());
		parent.getElement().appendChild(div);
	}
}
