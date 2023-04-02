package ${package}.websocketevents;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.web.bindery.event.shared.EventBus;
import ${package}.share.websocketevents.DemoEvent;

@Singleton
public class EventsBus implements DemoEvent.WebsocketEventHandler {
	private final EventBus eventBus;

	@Inject
	public EventsBus(final EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.addHandler(DemoEvent.getType(), this);
		UrlBuilder builder = Window.Location.createUrlBuilder();
		builder.setProtocol(Window.Location.getProtocol().startsWith("https") ? "wss": "ws").setHash(null);
		// TODO: websocket can not obtain session id on server side, we pass it by URL.
		String id = Cookies.getCookie("JSESSIONID");
		builder.setPath("/websocket/event/" + id);

		WebSocket connection = new WebSocket(builder.buildString());
		connection.onopen = e -> {
			GWT.log("socket open");
		};
		connection.onclose = e -> {
			GWT.log("socket close");
		};
		connection.onmessage = e -> {
			if(!(e.data instanceof String)) {
				GWT.log("message:" + e.data);
				return;
			}

			try {
				dupEvent((String)e.data);
			} catch (SerializationException e1) {
				GWT.log(e1.getMessage());
			}
		};
		connection.onerror = e -> {
			GWT.log("A transport error occurred - pass a error handler to your server builder to handle this yourself", new JavaScriptException(e));
		};
	}

	@SuppressWarnings("unchecked")
	private <E extends GwtEvent<?>> void dupEvent(String message) throws SerializationException {
		assert message.startsWith("//OK");//consider axing this, and the substring below
		EventSerializer serializer = GWT.create(EventSerializer.class);
		ClientSerializationStreamReader clientSerializationStreamReader = new ClientSerializationStreamReader(serializer.__getSerializer());
		clientSerializationStreamReader.prepareToRead(message.substring(4));
		Object object = clientSerializationStreamReader.readObject();
		
		if(object instanceof GwtEvent)
			eventBus.fireEvent((E)object);
	}

	@Override
	public void onWebsocketEvent(DemoEvent event) {
		GWT.log("fireEvent: " + event.message);
	}
}
