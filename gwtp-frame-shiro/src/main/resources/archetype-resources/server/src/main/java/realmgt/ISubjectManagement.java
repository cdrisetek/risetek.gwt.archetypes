package ${package}.realmgt;

import java.util.List;
import java.util.Set;

public interface ISubjectManagement {
	public List<SubjectEntity> ReadSubjects(final String like, int offset, int size);
	public boolean CreateSubjects(Set<SubjectEntity> subjects);
}
