package ${package}.root;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public interface RevealDefaultLinkColumnHandler extends EventHandler {
	
	void onRevealDefaultLinkColumn( RevealDefaultLinkColumnEvent event );
	
	static public class RevealDefaultLinkColumnEvent extends
	GwtEvent<RevealDefaultLinkColumnHandler> {

		private static Type<RevealDefaultLinkColumnHandler> TYPE;

		public static Type<RevealDefaultLinkColumnHandler> getType() {
			if (TYPE == null) {
				TYPE = new Type<RevealDefaultLinkColumnHandler>();
			}
			return TYPE;
		}
		
		
		@Override
		public Type<RevealDefaultLinkColumnHandler> getAssociatedType() {
			return getType();
		}

		@Override
		protected void dispatch(RevealDefaultLinkColumnHandler handler) {
			handler.onRevealDefaultLinkColumn( this );
		}
		
	}
}
