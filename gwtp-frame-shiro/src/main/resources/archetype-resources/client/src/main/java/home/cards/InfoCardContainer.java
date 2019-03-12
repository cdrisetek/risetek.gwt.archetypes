package ${package}.home.cards;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class InfoCardContainer {
	private static final StyleBundle.Style style = StyleBundle.resources.style();
	private final FlowPanel[] column = new FlowPanel[3];
	
	private Panel parent;
	public InfoCardContainer setParent(Panel parent) {
		this.parent = parent;
		return this;
	}
	public InfoCardContainer build() {
		style.ensureInjected();
		FlowPanel container = new FlowPanel();
		container.addStyleName(style.infoCardContainer());
		container.addStyleName(style.infoCardContainerCenter());
		container.addStyleName(style.infoCardContainerCenterNoWrap());
		container.addStyleName(style.startInserted());
		
		SimplePanel panelBody = new SimplePanel();
		panelBody.addStyleName(style.panelBody());
		panelBody.addStyleName(style.panelBodyScrollable());
		
		FlowPanel scrollContent = new FlowPanel();
		panelBody.add(scrollContent);
		scrollContent.addStyleName(style.panelBodyScrollContent());
		scrollContent.addStyleName(style.startInserted());
		scrollContent.add(container);
		parent.add(panelBody);
		
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
