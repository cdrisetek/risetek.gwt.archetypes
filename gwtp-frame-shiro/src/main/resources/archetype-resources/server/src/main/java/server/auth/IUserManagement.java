package ${package}.server.auth;

import java.util.List;

import ${package}.share.auth.UserEntity;
import ${package}.share.users.UserStateEntity;

public interface IUserManagement extends HasAuthorization, HasSubject, HasGod, HasAuthentication {
	public List<UserEntity> readUsers(final String like, int offset, int size) throws Exception;

	public void createUser(UserEntity user) throws Exception;
	public void setUserState(UserEntity user, UserStateEntity state) throws Exception;
}
