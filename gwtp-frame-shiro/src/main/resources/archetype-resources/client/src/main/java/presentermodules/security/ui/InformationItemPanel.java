package ${package}.presentermodules.security.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class InformationItemPanel extends Composite implements HasClickHandlers {
	interface MyUiBinder extends UiBinder<HTMLPanel, InformationItemPanel> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField DivElement key, value;
	@UiField Element link;
	
	public InformationItemPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.link.getStyle().setDisplay(Display.NONE);
	}

	public void hasLink() {
		this.link.getStyle().clearDisplay();
	}

	public void setKey(String key) {
		this.key.setInnerHTML(key);
	}

	public void setValue(String value) {
		this.value.setInnerHTML(value);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
}
