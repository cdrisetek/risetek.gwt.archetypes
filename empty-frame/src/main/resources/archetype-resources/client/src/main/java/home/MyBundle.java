package com.risetek.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MyBundle extends ClientBundle {
	
	public static MyBundle resources = GWT.create(MyBundle.class);
	
	interface Style extends CssResource {
		public String note_item();
		public String note_items();
		public String blueboard();
		public String go();
	}
	
	@Source("home.css")
	Style style();
}
