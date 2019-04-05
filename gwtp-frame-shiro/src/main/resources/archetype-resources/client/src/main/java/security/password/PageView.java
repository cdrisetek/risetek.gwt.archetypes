package ${package}.security.password;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;
import ${package}.utils.TagWrapBuilder;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	private final StyleBundle.Style style = StyleBundle.resources.style();

	@Inject
	public PageView() {
		style.ensureInjected();
		SimplePanel container = new SimplePanel();
		container.addStyleName(style.containerStyle());
		initWidget(container);

		Panel headBack = new TagWrapBuilder("div").wrap(new TagWrapBuilder("div")
				.wrap(new TagWrapBuilder(Icons.arrowLeftIcon(), "div").addStyleName(style.wrapBackIcon()).build())
				.addStyleName(style.centerIcon()).build()).addStyleName(style.headTitleBack()).build();

		headBack.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().goBack();
			}

		}, ClickEvent.getType());

		Panel headContent = new TagWrapBuilder("div").wrap(headBack).addStyleName(style.headTitleContent()).build();

		Panel headTitle1 = new TagWrapBuilder(headContent, "div").addStyleName(style.headTitle1()).build();

		Panel headTitle0 = new TagWrapBuilder("div").wrap(headTitle1).addStyleName(style.headTitle0()).build();
		headTitle0.add(new TagWrapBuilder("div").addStyleName(style.vSpace()).build());

		Panel listPanel = new TagWrapBuilder("div").wrap(headTitle0).addStyleName(style.infoLists()).build();

		container.add(listPanel);

		Panel headText = new TagWrapBuilder("h1").addStyleName(style.headTitleText()).build();
		headText.getElement().setInnerHTML("密码");
		headContent.add(headText);

		headTitle1.add(new TagWrapBuilder("div").addStyleName(style.headTitleSeparator()).build());
		// ----------------------------------------------------------------------------

		Panel mainNote = new TagWrapBuilder("div").addStyleName(style.mainNote())
				.wrap(new TagWrapBuilder("p").setText("请选择安全系数高的密码。").build()).build();
		mainNote.add(new TagWrapBuilder("p").setText("更改密码将会使您退出自己的设备。您重新登录时需要输入新密码。").build());

		Panel main = new TagWrapBuilder("div").addStyleName(style.mainFlow()).wrap(mainNote).build();
		listPanel.add(main);

		// input frame
		Panel inputFrame = new TagWrapBuilder("div").addStyleName(style.inputFrame()).build();
		listPanel.add(inputFrame);

		Panel inputArea = new TagWrapBuilder("div").addStyleName(style.inputArea()).build();
		Panel inputCommit = new TagWrapBuilder("div").addStyleName(style.inputCommit()).build();
		inputFrame.add(inputArea);
		inputFrame.add(inputCommit);
		// input Area
		Panel areaWiz = new TagWrapBuilder("div").addStyleName(style.inputAreaWiz()).build();
		inputArea.add(new TagWrapBuilder(areaWiz, "c-wiz").build());
		Panel commitTips = new TagWrapBuilder("div").addStyleName(style.commitTips())
				               .wrap(new TagWrapBuilder("div").addStyleName(style.commitTipsTitle())
				               .wrap(new TagWrapBuilder("span").setText("密码强度").addStyleName(style.commitTipsTitleContent()).build()).build()).build();
		
		commitTips.add(new TagWrapBuilder("div").setText("不要使用很容易被猜到的密码，也不要使用您用于登录其他网站的密码。").addStyleName(style.commitTipsContent()).build());
		
		areaWiz.add(passwordInput("新密码", true));
		areaWiz.add(commitTips);
		areaWiz.add(passwordInput("确认新密码", false));
		
		// input Commit
		Panel divButton = new TagWrapBuilder("div").role("button").addStyleName(style.commitButton())
				.wrap(new TagWrapBuilder("content").addStyleName(style.buttonContent())
						.wrap(new TagWrapBuilder("span").addStyleName(style.buttonText()).setText("更改密码").build())
						.build())
				.build();

		inputCommit.add(divButton);
		
		divButton.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().changePassword("newPassword");
				
			}}, ClickEvent.getType());
		
		divButton.addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				divButton.addStyleName(style.commitButtonDown());
			}}, MouseDownEvent.getType());
		
		divButton.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				divButton.removeStyleName(style.commitButtonDown());
			}}, MouseUpEvent.getType());
	}
	
	private Panel passwordInput(String tips, boolean checkvaild) {
		PasswordTextBox passwordInput = new PasswordTextBox();
		passwordInput.setStyleName(style.passwordInput());
		
		passwordInput.getElement().setAttribute("type", "password");
		passwordInput.getElement().setAttribute("autocomplete", "new-password");
		passwordInput.getElement().setAttribute("spellcheck", "false");
		passwordInput.getElement().setAttribute("autocapitalize", "off");
		passwordInput.getElement().setAttribute("autocorrect", "off");
		passwordInput.getElement().setAttribute("badinput", "false");
		
		Panel inputHolder = new TagWrapBuilder("div").addStyleName(style.inputHolder()).setText(tips).build();
		Panel inputWrap = new TagWrapBuilder("div").addStyleName(style.inputWrap()).build();
		inputWrap.add(passwordInput);
		inputWrap.add(inputHolder);
		
		Panel eyeContainer = new TagWrapBuilder("div").addStyleName(style.inputWithEyes()).build();
		eyeContainer.add(new TagWrapBuilder("div").wrap(inputWrap).addStyleName(style.inputWrapHeight()).build());

		Button eyeButton = new Button();
		eyeButton.setStyleName(style.buttonEye());
		eyeButton.getElement().appendChild(Icons.eyeSlashIcon());
		Panel buttonContent = new TagWrapBuilder("content").wrap(eyeButton).addStyleName(style.buttonEyeWrap()).build();
		inputWrap.add(buttonContent);
		
		Panel line1 = new TagWrapBuilder("div").addStyleName(style.inputUnderLine1()).build();
		Panel line = new TagWrapBuilder("div").addStyleName(style.inputUnderLine0()).wrap(line1).build();
		inputWrap.add(line);
		
		Panel inputLevel1 = new TagWrapBuilder("div").addStyleName(style.inputLevel1()).build();
		Panel inputLevel0 = new TagWrapBuilder("div").addStyleName(style.inputLevel0()).wrap(inputLevel1).build();

		inputLevel1.add(eyeContainer);
		
		Panel control = new TagWrapBuilder("div").wrap(inputLevel0).addStyleName(style.inputControl()).build();
		control.add(new TagWrapBuilder("div").addStyleName(style.inputControlPad()).build());

		passwordInput.addKeyUpHandler(e-> {
				if(passwordInput.getValue().isEmpty()) {
					inputWrap.removeStyleName(style.passwordInputNotEmpty());
					inputWrap.removeStyleName(style.passwordInvalid());
				} else
					inputWrap.addStyleName(style.passwordInputNotEmpty());
			});
		
		passwordInput.addFocusHandler(e-> {
				inputWrap.addStyleName(style.passwordInputFocus());
				inputWrap.removeStyleName(style.passwordInvalid());
		});
		
		passwordInput.addBlurHandler(e-> {

				inputWrap.removeStyleName(style.passwordInputFocus());
				if(!checkvaild)
					return;

				int len = passwordInput.getValue().length();
				if(len < 4 && len != 0)
					inputWrap.addStyleName(style.passwordInvalid());
				else
					inputWrap.removeStyleName(style.passwordInvalid());
			});
		
		eyeButton.addClickHandler(c->{
			eyeButton.getElement().removeAllChildren();
			if(passwordInput.getElement().getAttribute("type").equals("password")) {
				passwordInput.getElement().setAttribute("type", "text");
				eyeButton.getElement().appendChild(Icons.eyeIcon());
			} else {
				passwordInput.getElement().setAttribute("type", "password");
				eyeButton.getElement().appendChild(Icons.eyeSlashIcon());
			}
		});
		
		return control;
	}
}
