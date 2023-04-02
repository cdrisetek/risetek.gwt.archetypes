package ${package}.server.websocketevents;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import ${package}.share.websocketevents.DemoEvent;

@ServerEndpoint("/websocket/event/{id}")
public class EventsBus {
	private static final Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<String, Session>());
	private Method m;

	public EventsBus() {
		try {
			m = EventsBus.class.getDeclaredMethod("getEvent");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void onOpen(Session session, @PathParam("id") String id) {
		session.setMaxIdleTimeout(0);
		sessions.put(id, session);
		try {
			DemoEvent we = new DemoEvent(id);
			fireEvent(session, we);
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(@PathParam("id") String id) {
		sessions.remove(id);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
	}

	@OnError
	public void onError(Session session, Throwable error) {
	}

	public <E extends GwtEvent<?>> void broadcastEvent(E event) {
		for (Session s : sessions.values()) {
			try {
				fireEvent(s, event);
			} catch (SerializationException e) {
				e.printStackTrace();
			}
		}
	}

	public <E extends GwtEvent<?>> void fireEvent(Session session, E event) throws SerializationException {
		session.getAsyncRemote().sendText(encodeEvent(event), new SendHandler() {

			@Override
			public void onResult(SendResult result) {
				
			}
			
		});
	}

	// Just for Method provider used by RPC encoder.
	GwtEvent<?> getEvent() {
		return null;
	}

	protected static SerializationPolicy makePolicy() {
		return new SerializationPolicy() {
			@Override
			public void validateSerialize(Class<?> clazz) throws SerializationException {
			}
			@Override
			public void validateDeserialize(Class<?> clazz)
							throws SerializationException {
			}
			@Override
			public boolean shouldSerializeFields(Class<?> clazz) {
				return clazz != null;
			}
			@Override
			public boolean shouldDeserializeFields(Class<?> clazz) {
				return clazz != null;
			}
			public boolean shouldSerializeFinalFields() {
				return true;
			}
		};
	}

	private <E extends GwtEvent<?>> String encodeEvent(E event) throws SerializationException {
		return RPC.encodeResponseForSuccess(m, event, makePolicy());
	}
}
