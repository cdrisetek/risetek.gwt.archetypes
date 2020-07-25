package ${package}.server.realmgt;

import java.util.List;
import java.util.Set;
import org.apache.shiro.authc.AuthenticationToken;
import ${package}.share.realmgt.AccountEntity;

public interface ISubjectManagement {
	public List<AccountEntity> ReadSubjects(final String like, int offset, int size);
	public boolean CreateSubjects(Set<AccountEntity> subjects, String password);
	public boolean UpdateSubjects(Set<AccountEntity> subjects);
	public boolean checkValid(AuthenticationToken token);
	public Set<String> getRoles(String username);
}
