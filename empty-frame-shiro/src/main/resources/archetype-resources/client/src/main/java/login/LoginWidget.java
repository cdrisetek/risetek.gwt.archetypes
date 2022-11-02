package ${package}.login;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWidget extends SimplePanel {

	private final String loginTitle = "Login Title";
	private final String usernamePlaceholder = "enter username";
	private final String passwordPlaceholder = "enter password";
	private final String buttonLogin = "LOGIN";
	private final String buttonLogining = "Logining";

	private final String passwordTips = "password tips";
	private final String usernameTips = "username tips";

	private final String usernamePasswordInvalid = "username password invalid";
	
	public interface LoginSubmitHandle {
		public void onSubmit(String username, String password);
	}

	private final LoginSubmitHandle submitHandler;

	private final MyBundle.Style style = MyBundle.resources.style();
	private final FlowPanel backgroundPanel = new FlowPanel();
	private final SimplePanel title = new SimplePanel();

	private final FlowPanel username_flowpanel = new FlowPanel();
	private final TextBox usernameInputBox = new TextBox();
	private final SimplePanel username_tips = new SimplePanel();

	private final FlowPanel password_flowpanel = new FlowPanel();
	private final PasswordTextBox passwordInputBox = new PasswordTextBox();
	private final SimplePanel password_tips = new SimplePanel();
	
	private final Button loginSubmit = new Button();

	public LoginWidget(LoginSubmitHandle submithandler) {
		style.ensureInjected();
		setStyleName("loginWidget");
		// set myself on center
		getElement().setPropertyString("align", "center");

		submitHandler = submithandler;
		setSize("100%", "100%");
		
		loginSubmit.addClickHandler(event->{
			if (usernameInputBox.getValue() == null
					|| "".equals(usernameInputBox.getValue())) {
				username_tips.getElement().getStyle()
						.setDisplay(Display.BLOCK);
				usernameInputBox.setFocus(true);
				return;
			}
			if (passwordInputBox.getValue() == null
					|| "".equals(passwordInputBox.getValue())) {
				password_tips.getElement().getStyle()
						.setDisplay(Display.BLOCK);
				passwordInputBox.setFocus(true);
				return;
			}

			loginSubmit.setText(buttonLogining);
			submitHandler.onSubmit(usernameInputBox.getValue(), passwordInputBox.getValue());
		});

		backgroundPanel.setStyleName(style.background());

		Element div = DOM.createDiv();
		Element span = DOM.createSpan();
		span.setInnerHTML(loginTitle);
		div.setInnerHTML(span.getString());
		title.getElement().setInnerHTML(div.getString());
		title.setStyleName(style.topTitle());
		backgroundPanel.add(title);

		// Interval 0
		addInterval(backgroundPanel);

		// --- USERNAME BOX -----
		username_flowpanel.setStyleName(style.box_outer());
		username_flowpanel.addStyleName(style.box_outer_border());
		SimplePanel username_icon = new SimplePanel();
		username_icon.setStyleName(style.box_icon());
		Image user_img = new Image(MyBundle.resources.user_png());
		user_img.setStyleName(style.box_icon_img());
		username_icon.add(user_img);
		username_flowpanel.add(username_icon);
		SimplePanel username_input_panel = new SimplePanel();
		username_flowpanel.add(username_input_panel);
		username_input_panel.setStyleName(style.box_input());
		username_input_panel.add(usernameInputBox);

		username_flowpanel.add(username_tips);
		username_tips.setStyleName(style.box_tips());
		username_tips.getElement().setInnerText(usernameTips);
		username_tips.getElement().getStyle().setDisplay(Display.NONE);

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
		backgroundPanel.add(username_flowpanel);

		usernameInputBox.getElement().setPropertyString("placeholder", usernamePlaceholder);

		// Interval 1
		addInterval(backgroundPanel);

		// --- PASSWORD BOX ----
		password_flowpanel.setStyleName(style.box_outer());
		password_flowpanel.addStyleName(style.box_outer_border());

		SimplePanel password_icon = new SimplePanel();
		password_icon.setStyleName(style.box_icon());
		Image password_img = new Image(MyBundle.resources.password_png());
		password_img.setStyleName(style.box_icon_img());
		password_icon.add(password_img);
		password_flowpanel.add(password_icon);
		SimplePanel password_input_panel = new SimplePanel();
		password_flowpanel.add(password_input_panel);
		password_input_panel.setStyleName(style.box_input());
		password_input_panel.add(passwordInputBox);

		password_flowpanel.add(password_tips);
		password_tips.setStyleName(style.box_tips());
		password_tips.getElement().setInnerText(passwordTips);
		password_tips.getElement().getStyle().setDisplay(Display.NONE);

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
		backgroundPanel.add(password_flowpanel);
		passwordInputBox.getElement().setPropertyString("placeholder", passwordPlaceholder);

		// Interval 2
		addInterval(backgroundPanel);

		loginSubmit.setStyleName(style.loginButton());
		backgroundPanel.add(loginSubmit);

		add(backgroundPanel);
	}

	@Override
	protected void onLoad() {
		reset();
		passwordInputBox.setText(null);
		usernameInputBox.setFocus(true);
	}

	public void setFaliedStatus() {
		loginSubmit.setText(usernamePasswordInvalid);
	}

	private void reset() {
		loginSubmit.setText(buttonLogin);
		password_tips.getElement().getStyle().setDisplay(Display.NONE);
		username_tips.getElement().getStyle().setDisplay(Display.NONE);
	}
	
	private void addInterval(Panel panel) {
		Element div = DOM.createDiv();
		div.setClassName(style.interval());
		panel.getElement().appendChild(div);
	}
}
