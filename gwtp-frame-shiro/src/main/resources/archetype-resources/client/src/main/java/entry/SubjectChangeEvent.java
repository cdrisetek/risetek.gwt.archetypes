package ${package}.entry;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SubjectChangeEvent extends GwtEvent<SubjectChangeEvent.SubjectChangeHandler> {
	public static SubjectChangeEvent INSTANCE = new SubjectChangeEvent();
	protected SubjectChangeEvent() {}

	public interface SubjectChangeHandler extends EventHandler {
		public void onSubjectChange();
	}

	private static Type<SubjectChangeHandler> TYPE;
	
	public static Type<SubjectChangeHandler> getType() {
		if( TYPE == null )
			TYPE = new Type<SubjectChangeHandler>();
		
		return TYPE;
	}
	
	@Override
	public Type<SubjectChangeHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(SubjectChangeHandler handler) {
		handler.onSubjectChange();
	}
}
