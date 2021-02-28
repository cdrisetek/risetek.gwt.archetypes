package ${package}.place.root;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public interface RevealMenuEventHandler extends EventHandler {

	void onRevealDefaultLinkColumn(RevealMenuEvent event);

	static public class RevealMenuEvent extends GwtEvent<RevealMenuEventHandler> {

		private static Type<RevealMenuEventHandler> TYPE;

		public static Type<RevealMenuEventHandler> getType() {
			if (TYPE == null) {
				TYPE = new Type<RevealMenuEventHandler>();
			}
			return TYPE;
		}

		@Override
		public Type<RevealMenuEventHandler> getAssociatedType() {
			return getType();
		}

		@Override
		protected void dispatch(RevealMenuEventHandler handler) {
			handler.onRevealDefaultLinkColumn(this);
		}

	}
}
