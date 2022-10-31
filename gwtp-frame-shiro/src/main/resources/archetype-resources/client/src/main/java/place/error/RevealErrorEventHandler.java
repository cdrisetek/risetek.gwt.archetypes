package com.risetek.place.error;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public interface RevealErrorEventHandler extends EventHandler {

	void onRevealErrorPlace(RevealErrorEvent event);

	static public class RevealErrorEvent extends GwtEvent<RevealErrorEventHandler> {
		String exceptionName;
		private static Type<RevealErrorEventHandler> TYPE;

		public RevealErrorEvent(String exceptionName) {
			this.exceptionName = exceptionName;
		}

		public static Type<RevealErrorEventHandler> getType() {
			if (TYPE == null) {
				TYPE = new Type<RevealErrorEventHandler>();
			}
			return TYPE;
		}

		@Override
		public Type<RevealErrorEventHandler> getAssociatedType() {
			return getType();
		}

		@Override
		protected void dispatch(RevealErrorEventHandler handler) {
			handler.onRevealErrorPlace(this);
		}

	}
}
