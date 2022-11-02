package ${package}.presentermodules.security.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class InformationPanel extends Composite {
	interface MyUiBinder extends UiBinder<HTMLPanel, InformationPanel> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField HTMLPanel infolist;
	@UiField DivElement title;

	public InformationPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setTitle(String title) {
		this.title.setInnerHTML(title);
	}

	public void add(Widget panel) {
		infolist.add(panel);
	}
}
