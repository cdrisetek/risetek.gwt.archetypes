package ${package}.presentermodules.realmgt.subject;

import java.util.Set;
import com.gwtplatform.mvp.client.UiHandlers;
import ${package}.share.realmgt.AccountEntity;

interface MyUiHandlers extends UiHandlers {
	public void onSubjectPlace();
	public void onSubjectCreate(Set<AccountEntity> subjects, String password);
	public void onSubjectSearch(String like);
	public void onSubjectTablePagerFlush(boolean isResized, boolean forceLoad);
	public void onSubjectTablePager(int dir);
	public void onSubjectTablePagerHome();
	public void onSubjectMaintance(AccountEntity subject);
	public void onSubjectUpdate(Set<AccountEntity> subjects);
}
