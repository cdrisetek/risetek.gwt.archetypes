package ${package}.presentermodules.convert;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	public static final StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String pageStyle();
		public String frame();
		public String pageContentWrap();
		public String mainHeader();
		public String sourceTargetRow();
		public String convertWindow();
		public String textArea();
		public String translation();
		public String targetNote();
	}
	
	@Source("style.gss")
	Style style();
}
