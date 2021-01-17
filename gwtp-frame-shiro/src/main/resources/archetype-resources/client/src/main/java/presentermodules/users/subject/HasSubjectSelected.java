package ${package}.presentermodules.users.subject;

import com.google.gwt.event.shared.EventHandler;
import ${package}.share.auth.AccountEntity;

public interface HasSubjectSelected {
	public interface SubjectSeletedEventHandler extends EventHandler{
		void onSelected(AccountEntity subjectEntity);
	}

	public void addSubjectSelectedHandler(SubjectSeletedEventHandler handler);
}
