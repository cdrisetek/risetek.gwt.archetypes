package ${package}.presentermodules.realmgt.subject;

import com.google.gwt.event.shared.EventHandler;
import ${package}.share.realmgt.AccountEntity;

public interface HasSubjectSelected {
	public interface SubjectSeletedEventHandler extends EventHandler{
		void onSelected(AccountEntity subjectEntity);
	}

	public void addSubjectSelectedHandler(SubjectSeletedEventHandler handler);
}
