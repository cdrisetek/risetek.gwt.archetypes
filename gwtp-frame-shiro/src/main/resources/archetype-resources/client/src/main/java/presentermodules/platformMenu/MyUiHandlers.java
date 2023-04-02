package ${package}.presentermodules.platformMenu;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	void hideChooser();
	void showTip(AbstractDockMenu menu);
	void onIconClick(AbstractDockMenu menu);
	void gotoPlace(String token);
}
