package ${package}.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialValueBox;

public class SheetField {
	public interface ValidatorFunction<T> {
		void apply(T event);
	}	
	
	public enum TYPE {ACCOUNT, PASSWORD, EMAIL, TELPHONE}

	private SheetField next = null;

	private Widget field;
	private KeyUpHandler keyHandler;
	private List<ValidatorFunction<Consumer<Boolean>>> validator = new Vector<>();
	private boolean checkKeyPress = false;
	private int minLength = 0;
	
	public Builder nextField(Widget field) {
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

		public Builder type(TYPE type) {
			switch(type) {
			case ACCOUNT:
				assert (sheetField.field instanceof HasValue) : "password field not HasValue";
				assert (sheetField.field instanceof Focusable) : "password field not Focusable";

				break;
			case PASSWORD:
				assert (sheetField.field instanceof HasValue) : "password field not HasValue";
				assert (sheetField.field instanceof Focusable) : "password field not Focusable";

				break;
			case EMAIL:
				assert (sheetField.field instanceof HasValue) : "email field not HasValue";
				assert (sheetField.field instanceof Focusable) : "email field not Focusable";

				RegExp patter = RegExp.compile("^(.+)@(.+)$");
				sheetField.validator.add( c -> {
					@SuppressWarnings("unchecked")
					String value = ((HasValue<String>)sheetField.field).getValue();
					if((sheetField.minLength == 0) && (null == value || value.isEmpty()))
						c.accept(false);
					else
						c.accept(!patter.test(value));
				});
				break;
			case TELPHONE:
				break;
			default:
				assert false : "unsupport type";
				break;
			}
			return this;
		}

		public Builder checkKeyPress() {
			sheetField.checkKeyPress = true;
			return this;
		}

		public Builder minLength(int length) {
			assert length > 0 : "set min length must greate than 0";
			assert (sheetField.field instanceof HasValue) : "field not HasValue";
			sheetField.validator.add( isStop -> {
				@SuppressWarnings("unchecked")
				String value = ((HasValue<String>)sheetField.field).getValue();
				isStop.accept((null == value || value.length() < length));
			});
			return this;
		}

		public Builder regex(String pattern) {
			assert (sheetField.field instanceof HasValue) : "field not HasValue";
			RegExp patter = RegExp.compile(pattern);
			sheetField.validator.add( isStop -> {
				@SuppressWarnings("unchecked")
				String value = ((HasValue<String>)sheetField.field).getValue();
				if(patter.test(value))
					isStop.accept(true);
				else {
					isStop.accept(false);
				}
			});
			return this;
		}
		
		public Builder setKeyHandler(KeyUpHandler keyHandler) {
			sheetField.keyHandler = keyHandler;
			return this;
		}
		
		public Builder set(ValidatorFunction<Consumer<Boolean>> validator) {
			sheetField.validator.add(validator);
			return this;
		}
	}

	private void handlerKeyUp(KeyUpEvent e) {
		if(KeyCodes.KEY_ENTER == e.getNativeKeyCode()) {
			// check current field, if failed on validate, do select text action.
			// so target set to null.
			doValidate(validator.iterator(), null, (allpass) -> {
				if(next != null && next.field != null && next.field instanceof Focusable)
					((Focusable)next.field).setFocus(true);
			});
		} else if(checkKeyPress)
			doValidate(validator.iterator(), field, null);
	}

	private void doValidate(Iterator<ValidatorFunction<Consumer<Boolean>>> i, Widget target, ValidatorFunction<Object> passall) {
		if(i.hasNext()) {
			i.next().apply(isStoped -> {
				if(!isStoped)
					doValidate(i, target, passall);
				else if(target != field) {
					((Focusable)field).setFocus(true);
					if(field instanceof MaterialValueBox<?>) {
						@SuppressWarnings("unchecked")
						MaterialValueBox<String> box =((MaterialValueBox<String>)field);
						box.select();
					}
				}
			});
		} else {
			if(passall != null)
				passall.apply(null);
			if(null != next && target != field)
				next.validate(target);
		}
	}

	public void validate(Widget target) {
		if(field == target)
			return;

		doValidate(validator.iterator(), target, null);
	}
}
