package ${package}.security.email;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void changeEmail(String newEmail);
	public void goBack();
}
