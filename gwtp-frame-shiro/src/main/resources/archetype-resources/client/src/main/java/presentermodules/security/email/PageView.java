package ${package}.presentermodules.security.email;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.presentermodules.security.ui.TitlePanel;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	interface MyUiBinder extends UiBinder<HTMLPanel, PageView> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField TitlePanel titlePanel;
	@UiField TextBox textbox;
	@UiField Panel commitButton;

	@Inject
	public PageView() {
		initWidget(uiBinder.createAndBindUi(this));
		textbox.getElement().setAttribute("role", "button");
		textbox.getElement().setAttribute("spellcheck", "false");
		textbox.getElement().setAttribute("autocapitalize", "off");
		textbox.getElement().setAttribute("autocorrect", "off");
		textbox.getElement().setAttribute("badinput", "false");
		
		titlePanel.addClickHandler(c-> getUiHandlers().goContinue());
		commitButton.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().changeEmail(textbox.getValue());
			}}, ClickEvent.getType());
	}
	
	@Override
	protected void onAttach() {
		textbox.setValue(getUiHandlers().getOriginEmail());
	}
}
