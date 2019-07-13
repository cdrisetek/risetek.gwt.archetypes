package ${package}.home.cards;

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
		public String infoCardContainer();
		public String infoCardContainerCenter();
		public String infoCardContainerCenterNoWrap();
		public String startInserted();
		public String panelBody();
		public String panelBodyScrollable();
		public String panelBodyScrollContent();
		public String infoCardColumn();
		public String infoCardColumnWidth1();
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
		column[0] = new InfoCardContainer.ColumnBuilder();
		container.add(column[0]);
		column[1] = new InfoCardContainer.ColumnBuilder();
		container.add(column[1]);
		column[2] = new InfoCardContainer.ColumnBuilder();
		container.add(column[2]);
		
		return this;
	}

	public void addToColumn(IsWidget content, int index) {
		column[index].add(content);
	}
	
	private static class ColumnBuilder extends FlowPanel {
		public ColumnBuilder() {
			style.ensureInjected();
			addStyleName(style.infoCardColumn());
			addStyleName(style.infoCardColumnWidth1());
			addStyleName(style.startInserted());
		}
	}
}
