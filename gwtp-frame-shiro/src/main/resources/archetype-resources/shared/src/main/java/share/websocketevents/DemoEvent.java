package ${package}.share.websocketevents;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DemoEvent extends GwtEvent<DemoEvent.WebsocketEventHandler> implements IsSerializable {

	DemoEvent() {}

	public String message;
	public DemoEvent(String message) {
		this.message = message;
	}

	public interface WebsocketEventHandler extends EventHandler {
		public void onWebsocketEvent(DemoEvent event);
	}

	private static Type<WebsocketEventHandler> TYPE;
	
	public static Type<WebsocketEventHandler> getType() {
		if( TYPE == null )
			TYPE = new Type<WebsocketEventHandler>();
		
		return TYPE;
	}

	@Override
	public Type<WebsocketEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(WebsocketEventHandler handler) {
		handler.onWebsocketEvent(this);
	}
}
