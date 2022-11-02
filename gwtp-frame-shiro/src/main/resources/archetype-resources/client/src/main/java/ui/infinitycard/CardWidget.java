package ${package}.ui.infinitycard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class CardWidget<E> extends SimplePanel implements HasClickHandlers {
	public CardWidget(E entity) {
		this.entity = entity;
	}

	private E entity;
	
	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

	public boolean visible = false;
    public void onShow() {};
    public void onHide() {};

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return addDomHandler(handler, ClickEvent.getType());
	}
}
