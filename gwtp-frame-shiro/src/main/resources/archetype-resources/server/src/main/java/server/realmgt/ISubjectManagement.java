package ${package}.server.realmgt;

import java.util.List;
import java.util.Set;
import org.apache.shiro.authc.AuthenticationToken;
import ${package}.share.exception.ActionUnauthorizedException;
import ${package}.share.realmgt.SubjectEntity;

public interface ISubjectManagement {
	public List<SubjectEntity> ReadSubjects(final String like, int offset, int size);
	public boolean CreateSubjects(Set<SubjectEntity> subjects);
	public boolean checkValid(AuthenticationToken token);
	public Set<String> getRoles(String username);
}
