package ${package}.login;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AuthorityChangedEvent extends GwtEvent<AuthorityChangedEvent.AuthorityChangedHandler> {
	public static AuthorityChangedEvent INSTANCE = new AuthorityChangedEvent();
	public interface AuthorityChangedHandler extends EventHandler {
		public void onAuthorityChanged();
	}

	private static Type<AuthorityChangedHandler> TYPE;
	
	public static Type<AuthorityChangedHandler> getType() {
		if( TYPE == null )
			TYPE = new Type<AuthorityChangedHandler>();
		
		return TYPE;
	}
	
	@Override
	public Type<AuthorityChangedHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(AuthorityChangedHandler handler) {
		handler.onAuthorityChanged();
	}

}
