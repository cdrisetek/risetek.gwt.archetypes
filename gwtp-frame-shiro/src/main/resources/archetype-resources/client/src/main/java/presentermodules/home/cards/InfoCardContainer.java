package ${package}.presentermodules.home.cards;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;

public class InfoCardContainer {
	interface MyStyle extends CssResource {
		public String homeCardColumn();
		public String homeCardColumnWidth();
	}

	interface MyUiBinder extends UiBinder<HTMLPanel, InfoCardContainer> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField static MyStyle style;
	@UiField HTMLPanel container;

	private final FlowPanel[] column = new FlowPanel[3];
	
	private Panel parent;
	public InfoCardContainer setParent(Panel parent) {
		this.parent = parent;
		return this;
	}
	public InfoCardContainer build() {
		parent.add(uiBinder.createAndBindUi(this));
		style.ensureInjected();
		column[0] = ColumnPanel();
		container.add(column[0]);
		column[1] = ColumnPanel();
		container.add(column[1]);
		column[2] = ColumnPanel();
		container.add(column[2]);
		return this;
	}

	public void addToColumn(IsWidget content, int index) {
		column[index].add(content);
	}
	
	private FlowPanel ColumnPanel() {
		FlowPanel panel = new FlowPanel();
		style.ensureInjected();
		panel.addStyleName(style.homeCardColumn());
		panel.addStyleName(style.homeCardColumnWidth());
		return panel;
	}
}
