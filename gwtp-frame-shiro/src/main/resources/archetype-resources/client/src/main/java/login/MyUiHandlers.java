package ${package}.login;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void Login(String username, String password, boolean rememberme);
}
