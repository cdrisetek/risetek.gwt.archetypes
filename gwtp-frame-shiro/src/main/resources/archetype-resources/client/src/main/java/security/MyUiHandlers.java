package ${package}.security;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public HashMap<String, String> getSecurityInformation();
	public HashMap<String, String> getContactInformation();
	public void update(String name);
}
