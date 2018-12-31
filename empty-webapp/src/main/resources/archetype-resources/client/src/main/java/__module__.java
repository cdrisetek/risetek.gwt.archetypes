package ${package};

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import ${package}.generator.IBuilderStamp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ${module} implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		String buildStamp = ((IBuilderStamp)GWT.create(IBuilderStamp.class)).getBuilderStamp();
		
		RootPanel.get().add(new HTML(buildStamp));
	}
}
