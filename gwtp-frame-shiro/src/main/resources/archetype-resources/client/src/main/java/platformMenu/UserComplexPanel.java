package ${package}.platformMenu;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * help class for user define tag
 * @author wangyc@risetek.com
 *
 */

public class UserComplexPanel extends ComplexPanel {
	public UserComplexPanel(String tag) {
		setElement(Document.get().createElement(tag));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void add(Widget w) {
		add(w, getElement());
	}
}
