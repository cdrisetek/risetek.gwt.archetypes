package ${package}.presentermodules.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

interface StyleLogin extends ClientBundle {
	
	public static StyleLogin resources = GWT.create(StyleLogin.class);
	
	interface Style extends CssResource {
		public String loginWidgetContainer();
	}
	@Source("style.gss")
	Style style();
}
