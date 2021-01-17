package ${package}.presentermodules.users.subject;

import java.util.Set;
import com.gwtplatform.mvp.client.UiHandlers;
import ${package}.share.auth.AccountEntity;

interface MyUiHandlers extends UiHandlers {
	public void onUserCreatePlace();
	public void onUserMaintancePlace(AccountEntity subject);

	public void onUserCreate(Set<AccountEntity> subjects, String password);
	public void onUserSearch(String like);
	public void onUserUpdate(Set<AccountEntity> subjects);

	public void onSubjectTablePagerFlush(boolean isResized, boolean forceLoad);
	public void onSubjectTablePager(int dir);
	public void onSubjectTablePagerHome();
}
