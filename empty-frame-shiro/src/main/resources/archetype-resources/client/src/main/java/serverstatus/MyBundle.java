package ${package}.serverstatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

interface MyBundle extends ClientBundle {
	
	static MyBundle resources = GWT.create(MyBundle.class);
	
	interface Style extends CssResource {
		public String cards();
		public String card();
		public String title();
	}
	
	@Source("cardstyle.gss")
	Style style();
}
