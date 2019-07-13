package ${package}.security.password;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.security.ui.TitlePanel;
import ${package}.utils.Icons;
import ${package}.utils.TagWrapBuilder;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface MyStyle extends CssResource {
		String passwordInput();
		String inputHolder();
		String inputWrap();
		String inputWithEyes();
		String inputWrapHeight();
		String inputUnderLine0();
		String inputUnderLine1();
		String inputLevel0();
		String inputLevel1();
		String buttonEye();
		String inputControl();
		String buttonEyeWrap();
		String passwordInputNotEmpty();
		String passwordInvalid();
		String passwordInputFocus();
		String inputControlPad();
		String commitButtonDown();
	}
	
	interface MyUiBinder extends UiBinder<HTMLPanel, PageView> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField MyStyle style;
	@UiField TitlePanel titlePanel;
	@UiField Panel inputwrap1;
	@UiField Panel inputwrap2;
	@UiField Panel commitButton;
	@UiField DivElement controlpad1;
	@UiField DivElement controlpad2;
	
	private final Password passwordInput0 = new Password();
	private final Password passwordInput1 = new Password();
	
	@Inject
	public PageView() {

		initWidget(uiBinder.createAndBindUi(this));
		titlePanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().goContinue();
			}});

		inputwrap1.add(passwordInput0.makePanel("新密码", true, controlpad1));
		inputwrap2.add(passwordInput1.makePanel("确认新密码", false, controlpad2));
		
		commitButton.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(!passwordInput0.getValue().isEmpty() && passwordInput0.getValue().equals(passwordInput1.getValue()))
					getUiHandlers().changePassword(passwordInput0.getValue());
				else
					passwordInput1.showValidTip("密码不一致。");
			}}, ClickEvent.getType());
		
		commitButton.addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				commitButton.addStyleName(style.commitButtonDown());
			}}, MouseDownEvent.getType());
		
		commitButton.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				commitButton.removeStyleName(style.commitButtonDown());
			}}, MouseUpEvent.getType());
	}

	@Override
	public void onAttach() {
		passwordInput0.reset();
		passwordInput1.reset();
		passwordInput0.setDefaultFocus();
	}

	class Password  {
		private PasswordTextBox passwordInput;
		private Panel inputWrap;
		private SimplePanel eyeButton;
		private DivElement tipDiv;
		public Panel makePanel(String tips, boolean checkvaild, DivElement tipDiv) {
			this.tipDiv = tipDiv;
			passwordInput = new PasswordTextBox();
			passwordInput.setStyleName(style.passwordInput());

			passwordInput.getElement().setAttribute("type", "password");
			passwordInput.getElement().setAttribute("autocomplete", "new-password");
			passwordInput.getElement().setAttribute("spellcheck", "false");
			passwordInput.getElement().setAttribute("autocapitalize", "off");
			passwordInput.getElement().setAttribute("autocorrect", "off");
			passwordInput.getElement().setAttribute("badinput", "false");
			
			inputWrap = new TagWrapBuilder("div").addStyleName(style.inputWrap()).build();
			inputWrap.add(passwordInput);
			inputWrap.add(new TagWrapBuilder("div").addStyleName(style.inputHolder()).setText(tips).build());
			
			eyeButton = new SimplePanel();
			eyeButton.setStyleName(style.buttonEye());
			eyeButton.getElement().setAttribute("role", "button");
			inputWrap.add(new TagWrapBuilder("content").wrap(eyeButton).addStyleName(style.buttonEyeWrap()).build());
			
			Panel line1 = new TagWrapBuilder("div").addStyleName(style.inputUnderLine1()).build();
			Panel line = new TagWrapBuilder("div").addStyleName(style.inputUnderLine0()).wrap(line1).build();
			inputWrap.add(line);

			passwordInput.addKeyUpHandler(e-> {
				if(passwordInput.getValue().isEmpty()) {
					inputWrap.removeStyleName(style.passwordInputNotEmpty());
					inputWrap.removeStyleName(style.passwordInvalid());
				} else
					inputWrap.addStyleName(style.passwordInputNotEmpty());
			});
			
			passwordInput.addFocusHandler(e-> clearValidTip());
			
			passwordInput.addBlurHandler(e-> {

				inputWrap.removeStyleName(style.passwordInputFocus());
				if(!checkvaild)
					return;

				int len = passwordInput.getValue().length();
				if(len < 4 && len != 0)
					showValidTip("密码太短。");
				else
					clearValidTip();
			});
			
			eyeButton.addDomHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					eyeButton.getElement().removeAllChildren();
					if(passwordInput.getElement().getAttribute("type").equals("password")) {
						passwordInput.getElement().setAttribute("type", "text");
						eyeButton.getElement().appendChild(Icons.eyeIcon());
					} else {
						passwordInput.getElement().setAttribute("type", "password");
						eyeButton.getElement().appendChild(Icons.eyeSlashIcon());
					}
				}}, ClickEvent.getType());
			return inputWrap;
		}
		
		public void setDefaultFocus() {
			passwordInput.setFocus(true);
		}
		
		public String getValue() {
			return passwordInput.getValue();
		}
		
		public void clearValidTip() {
			inputWrap.addStyleName(style.passwordInputFocus());
			inputWrap.removeStyleName(style.passwordInvalid());
			tipDiv.setInnerHTML(null);
		}

		public void showValidTip(String tip) {
			inputWrap.addStyleName(style.passwordInvalid());
			tipDiv.setInnerHTML(tip);
		}

		public void reset() {
			passwordInput.setValue(null);
			inputWrap.removeStyleName(style.passwordInvalid());
			passwordInput.getElement().setAttribute("type", "password");
			eyeButton.getElement().removeAllChildren();
			eyeButton.getElement().appendChild(Icons.eyeSlashIcon());
		}
	}
}
