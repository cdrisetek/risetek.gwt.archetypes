package ${package}.security.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

public class TitlePanel extends Composite implements HasClickHandlers {
	interface MyUiBinder extends UiBinder<HTMLPanel, TitlePanel> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField Panel backButton;
	@UiField HeadingElement title;

	public @UiConstructor TitlePanel(String title) {
		initWidget(uiBinder.createAndBindUi(this));
		this.title.setInnerHTML(title);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return backButton.addDomHandler(handler, ClickEvent.getType());
	}
}
