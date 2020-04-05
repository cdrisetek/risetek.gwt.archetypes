package ${package}.presentermodules.security.password;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void changePassword(String newPassword);
	public void goContinue();
}
