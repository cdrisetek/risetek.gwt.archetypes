package ${package}.login;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UserRolesChangeEvent extends GwtEvent<UserRolesChangeEvent.UserRolesChangeHandler> {
	public static UserRolesChangeEvent INSTANCE = new UserRolesChangeEvent();
	public interface UserRolesChangeHandler extends EventHandler {
		public void onUserStatusChange();
	}

	private static Type<UserRolesChangeHandler> TYPE;
	
	public static Type<UserRolesChangeHandler> getType() {
		if( TYPE == null )
			TYPE = new Type<UserRolesChangeHandler>();
		
		return TYPE;
	}
	
	@Override
	public Type<UserRolesChangeHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(UserRolesChangeHandler handler) {
		handler.onUserStatusChange();
	}
}
