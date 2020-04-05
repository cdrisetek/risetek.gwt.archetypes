package ${package}.presentermodules.security.email;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public String getOriginEmail();
	public void changeEmail(String newEmail);
	public void goContinue();
}
