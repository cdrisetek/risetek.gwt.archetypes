package ${package}.presentermodules.users.subject;

import java.util.Set;
import com.gwtplatform.mvp.client.UiHandlers;
import ${package}.share.auth.UserEntity;

interface MyUiHandlers extends UiHandlers {
	public void onUserCreatePlace();
	public void onUserMaintancePlace(UserEntity subject);

	public void onUserCreate(Set<UserEntity> subjects, String password);
	public void onUserSearch(String like);
	public void onUserUpdate(Set<UserEntity> subjects);

	public void onSubjectTablePagerFlush(boolean isResized, boolean forceLoad);
	public void onSubjectTablePager(int dir);
	public void onSubjectTablePagerHome();
}
