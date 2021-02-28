package ${package}.utils;

import java.util.function.Consumer;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

public class SheetField {
	public interface ValidatorFunction<T> {
		void apply(T event);
	}	
	
	private SheetField next = null;

	private Widget field;
	private KeyUpHandler keyHandler;
	private ValidatorFunction<Consumer<Boolean>> validator;
	private boolean doValidator = false;

	public Builder nextBuilder(Widget field) {
		return new Builder(field, this);
	}
	
	public static class Builder {
		SheetField sheetField = new SheetField();
		public SheetField build() {
			if(parent != null)
				parent.next = sheetField;
			if(sheetField.field instanceof HasKeyUpHandlers) {
				if(sheetField.keyHandler != null)
					((HasKeyUpHandlers)sheetField.field).addKeyUpHandler(sheetField.keyHandler);
				else
					((HasKeyUpHandlers)sheetField.field).addKeyUpHandler(keyEvent -> sheetField.handlerKeyUp(keyEvent));
			}
			return sheetField;
		}

		private SheetField parent;
		public Builder(Widget field, SheetField parent) {
			this.parent = parent;
			sheetField.field = field;
		}
		
		public Builder(Widget field) {
			sheetField.field = field;
		}

		public Builder doValidator() {
			sheetField.doValidator = true;
			return this;
		}

		public Builder setKeyHandler(KeyUpHandler keyHandler) {
			sheetField.keyHandler = keyHandler;
			return this;
		}
		
		public Builder set(ValidatorFunction<Consumer<Boolean>> fnValidator) {
			sheetField.validator = fnValidator;
			return this;
		}
	}

	private void handlerKeyUp(KeyUpEvent e) {
		if(KeyCodes.KEY_ENTER == e.getNativeKeyCode()) {
			if(next != null && next.field != null && next.field instanceof Focusable)
				((Focusable)next.field).setFocus(true);
		} else if(doValidator)
			validator.apply(c -> {/* do nothing */});
	}

	public void nestedCheck(Widget target) {
		if(validator != null) {
			validator.apply(stoped -> {
				if(!stoped && field != target && null != next)
					next.nestedCheck(target);
			});
		} else if(field != target && null != next)
			next.nestedCheck(target);
	}
	
	public SheetField appendTo(SheetField parent) {
		parent.next = this;
		return this;
	}
}
