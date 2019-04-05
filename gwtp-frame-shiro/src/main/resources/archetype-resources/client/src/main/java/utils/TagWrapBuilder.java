package ${package}.utils;

import java.util.List;
import java.util.Vector;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class TagWrapBuilder {
	private String tag;
	private Object target;
	private String role;
	private String text;
	private List<String> styles = new Vector<>();

	public TagWrapBuilder(String tag) {
		this.tag = tag;
	}
	
	public TagWrapBuilder(Element target, String tag) {
		this.target = target;
		this.tag = tag;
	}

	public TagWrapBuilder(Widget target, String tag) {
		this.target = target;
		this.tag = tag;
	}

	public TagWrapBuilder addStyleName(String style) {
		styles.add(style);
		return this;
	}

	public TagWrapBuilder wrap(Element target) {
		this.target = target;
		return this;
	}

	public TagWrapBuilder wrap(Widget target) {
		this.target = target;
		return this;
	}

	public TagWrapBuilder role(String role) {
		this.role = role;
		return this;
	}

	public TagWrapBuilder setText(String text) {
		this.text = text;
		return this;
	}

	public Panel build() {
		ComplexPanel tagWrap = new Wrap(tag);
		for (String style : styles)
			tagWrap.addStyleName(style);

		if(null != role)
			tagWrap.getElement().setAttribute("role", role);

		if(null != text)
			tagWrap.getElement().setInnerText(text);

		if(null != target) {
			if(target instanceof Widget)
				tagWrap.add((Widget)target);
			else
				tagWrap.getElement().appendChild((Element)target);
		}
		return tagWrap;
	}

	private static class Wrap extends ComplexPanel {
		private Wrap(String tag) {
			setElement(Document.get().createElement(tag));
		}

		@SuppressWarnings("deprecation")
		@Override
		public void add(Widget w) {
			add(w, getElement());
		}
	}
}
