package ${package}.security;

import java.util.HashMap;
import java.util.Map;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public HashMap<String, Map<String, String>> getSecurityInformation();
	public HashMap<String, Map<String, String>> getContactInformation();
	public void update(String name);
}
