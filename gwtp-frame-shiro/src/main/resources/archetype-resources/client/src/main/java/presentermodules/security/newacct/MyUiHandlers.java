package ${package}.presentermodules.security.newacct;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public void newAccount(String account, String password);
	public void goContinue();
}
