package com.risetek.home;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class InfoCard extends SimplePanel {

	private static final StyleBundle.Style style = StyleBundle.resources.style();

	private final Label label = new Label();
	private final FlowPanel cardBodyGroup = new FlowPanel();
	private final SimplePanel iconPanel = new SimplePanel();
	private final FlowPanel loaderContent = new FlowPanel();

	public InfoCard() {
		setStyleName(style.startInserted());
		SimplePanel infoCard = new SimplePanel();
		add(infoCard);
		infoCard.addStyleName(style.startInserted());
		infoCard.addStyleName(style.infoCard());
		FlowPanel infoCardStyling = new FlowPanel();
		infoCard.add(infoCardStyling);
		infoCardStyling.addStyleName(style.infoCardStyling());
		infoCardStyling.addStyleName(style.startInserted());
		
		// Top Right
		SimplePanel topRight = new SimplePanel();
		infoCardStyling.add(topRight);
		topRight.addStyleName(style.infoCardTopRight());
		topRight.addStyleName(style.startInserted());
		
		// Card Header
		FlowPanel cardHeader = new FlowPanel();
		infoCardStyling.add(cardHeader);
		cardHeader.addStyleName(style.infoCardHeader());
		SimplePanel gutter = new SimplePanel();
		gutter.addStyleName(style.infoCardItemLeftGutter());
		cardHeader.add(gutter);
		//   HeaderIcon
		gutter.add(iconPanel);
		iconPanel.addStyleName(style.matIcon());
		
		FlowPanel headContent = new FlowPanel();
		cardHeader.add(headContent);
		headContent.addStyleName(style.headContent());
		headContent.add(label);
		label.setStyleName(style.headText());
		
		// Card content
		SimplePanel cardLoad = new SimplePanel();
		infoCardStyling.add(cardLoad);
		cardLoad.addStyleName(style.cfcLoader());
		cardLoad.add(loaderContent);
		loaderContent.addStyleName(style.loaderContent());
		loaderContent.addStyleName(style.startInserted());
		
		loaderContent.add(cardBodyGroup);
		cardBodyGroup.addStyleName(style.cardBodyGroup());
	}
	
	public InfoCard updateInfoItems(List<InfoItem> items) {
		cardBodyGroup.clear();

		for(InfoItem item:items) {
			FlowPanel itemPanel = new FlowPanel();
			itemPanel.addStyleName(style.infoCardItem());
			itemPanel.addStyleName(style.startInserted());
			Element gutter = DOM.createDiv();
			gutter.setClassName(style.infoCardItemLeftGutter());
			itemPanel.getElement().appendChild(gutter);

			SimplePanel infoCardContent = new SimplePanel();
			infoCardContent.addStyleName(style.InfoCardItemContent());
			itemPanel.add(infoCardContent);
			FlowPanel infoCardContentLeft = new FlowPanel();
			infoCardContent.add(infoCardContentLeft);
			infoCardContentLeft.addStyleName(style.infoCardItemContentLeft());
			
			if(null != item.infoText) {
				Element textLine = DOM.createDiv();
				textLine.setClassName(style.infoTextLine());
				
				Label text = new Label(item.infoText);
				text.setStyleName(style.cardInfoText());
				textLine.appendChild(text.getElement());
				
				infoCardContentLeft.getElement().appendChild(textLine);
			}
			if(null != item.infoTextSecondary) {
				Element textLine = DOM.createDiv();
				textLine.setClassName(style.infoTextLine());

				Label text = new Label(item.infoTextSecondary);
				text.setStyleName(style.cardInfoText());
				text.addStyleName(style.cardInfoTextSecondary());
				textLine.appendChild(text.getElement());

				infoCardContentLeft.getElement().appendChild(textLine);
			}
			cardBodyGroup.add(itemPanel);
		}
		return this;
	}

	private SimplePanel cardRedirect = null;
	public InfoCard updateRedirect(String title, String link) {
		if(null != cardRedirect)
			cardRedirect.removeFromParent();

		appendRedirect(title, link);
		return this;
	}
	
	
	private InfoCard appendRedirect(String redirectTitle, String redirectLink) {
		if(null == redirectTitle || null == redirectLink)
			return this;

		style.ensureInjected();
		// Redirect
		cardRedirect = new SimplePanel();
		loaderContent.add(cardRedirect);
		
		Element link = DOM.createAnchor();
		cardRedirect.getElement().appendChild(link);
		link.setClassName(style.cardRedirect() + " " + style.infoCardItem());
		link.setAttribute("href", redirectLink);
		Element span = DOM.createSpan();
		Element icon = DOM.createDiv();
		icon.setClassName(style.matIcon());
		span.appendChild(icon);
		icon.appendChild(arrowIcon());

		link.appendChild(span);
		span.setClassName(style.infoCardItemLeftGutter());
		Element p = DOM.createDiv();
		p.setClassName(style.InfoCardItemContent());
		p.setInnerHTML(redirectTitle);
		link.appendChild(p);
		return this;
	}

	public static class Builder {
		private String title;
		private Panel parent;
		private Element icon;
		
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setIcon(Element icon) {
			this.icon = icon;
			return this;
		}
		
		private String redirectTitle;
		private String redirectLink;
		public Builder setRedirect(@NotNull String title, @NotNull String link) {
			redirectTitle = title;
			redirectLink = link;
			return this;
		}

		public Builder setParent(Panel parent) {
			this.parent = parent;
			return this;
		}
		public InfoCard build() {
			style.ensureInjected();
			InfoCard panel = new InfoCard();
			panel.label.setText(title);
			parent.add(panel);
			if(null != icon)
				panel.iconPanel.getElement().appendChild(icon);
			
			panel.appendRedirect(redirectTitle, redirectLink);
			return panel;
		}
	}
	
	static class ContainerBuilder {
		private Panel parent;
		
		public ContainerBuilder setParent(Panel parent) {
			this.parent = parent;
			return this;
		}
		public FlowPanel build() {
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
			return container;
		}
	}

	static class ColumnBuilder {
		private Panel parent;
		public ColumnBuilder setParent(Panel parent) {
			this.parent = parent;
			return this;
		}

		public FlowPanel build() {
			style.ensureInjected();
			FlowPanel column = new FlowPanel();
			column.addStyleName(style.infoCardColumn());
			column.addStyleName(style.infoCardColumnWidth1());
			column.addStyleName(style.startInserted());
			parent.add(column);
			return column;
		}
	}

    private static native Element arrowIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z");
        e.appendChild(p);
        return e;
    }-*/;

    public static class InfoItem {
    	String infoText;
    	String infoTextSecondary;
    }
}
