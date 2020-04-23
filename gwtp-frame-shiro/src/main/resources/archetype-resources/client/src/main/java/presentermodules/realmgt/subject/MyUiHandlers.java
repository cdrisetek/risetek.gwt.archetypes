package ${package}.presentermodules.realmgt.subject;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void onCreateSubject();
	public void onSearch(String like);
	public void onSubjectTablePagerFlush(boolean isResized, boolean forceLoad);
	public void onSubjectTablePager(int dir);
	public void onSubjectTablePagerHome();
}
