package ${package}.presentermodules.security;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public List<informationItem> getSecurityInformation();
	public List<informationItem> getContactInformation();
	public void update(String name);
	
	public class informationItem {
		String key;
		String value;
		String link;
	}
}
