package ${package}.security;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	public static final StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String containerStyle();
		public String infoLists();
		public String header();
		public String headerTitle();
		public String headerDescript();
		public String article();
		public String articleTitle();
		public String articleTitleHeader();
		public String articleBody();
		public String infoPice();
		public String anchorStyle();
		public String anchorStyleInside();
		public String anchorFlex();
		public String anchorKey();
		public String anchorKeyH();
		public String anchorValue();
		public String anchorValueH();
		public String anchorArrow();
		public String anchorKeyValue();
		public String separator();
		public String separatorBorder();
	}
	
	@Source("style.gss")
	Style style();
}
