package ${package}.ui.infinitycard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;

public class CardPanel extends HTMLPanel implements HasClickHandlers {

	public CardPanel(String html) {
		super(html);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return addDomHandler(handler, ClickEvent.getType());
	}
}
