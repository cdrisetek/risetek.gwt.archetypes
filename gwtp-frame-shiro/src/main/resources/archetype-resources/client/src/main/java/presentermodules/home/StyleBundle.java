package ${package}.presentermodules.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	public static final StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String homeStyle();
	}
	
	@Source("style.gss")
	Style style();
}
