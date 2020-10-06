package ${package}.presentermodules.users.subject;

import com.google.gwt.event.shared.EventHandler;
import ${package}.share.auth.UserEntity;

public interface HasSubjectSelected {
	public interface SubjectSeletedEventHandler extends EventHandler{
		void onSelected(UserEntity subjectEntity);
	}

	public void addSubjectSelectedHandler(SubjectSeletedEventHandler handler);
}
