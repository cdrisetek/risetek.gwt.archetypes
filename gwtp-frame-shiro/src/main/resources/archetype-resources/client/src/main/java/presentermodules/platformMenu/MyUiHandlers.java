package ${package}.presentermodules.platformMenu;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	void hideChooser();
	void showTip(AbstractPlatformBarMenu menu);
	void onIconClick(AbstractPlatformBarMenu menu);
	void gotoPlace(String token);
}
