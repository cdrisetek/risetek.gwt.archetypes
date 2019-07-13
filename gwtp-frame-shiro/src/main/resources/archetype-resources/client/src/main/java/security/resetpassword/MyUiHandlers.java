package ${package}.security.resetpassword;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void resetPassword(String email);
	public void goContinue();
}
