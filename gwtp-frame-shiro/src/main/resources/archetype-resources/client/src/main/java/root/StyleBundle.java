package ${package}.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

interface StyleBundle extends ClientBundle {
	
	static StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String p6nVulcanLayoutMain();
		public String layoutColumn();
		public String flex();
		public String flex_none();
		public String pan_shell_top_container();
		public String panShellMainContainer();
		public String layoutRow();
		public String layout();
	}
	
	@Source("style.gss")
	Style style();
}
