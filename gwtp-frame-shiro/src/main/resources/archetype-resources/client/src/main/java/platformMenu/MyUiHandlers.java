package ${package}.platformMenu;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	void removeMenuPanel();
	void showTip(AbstractPlatformBarMenu menu);
	void onMenuClick(AbstractPlatformBarMenu menu);
	void gotoPlace(String token);
}
