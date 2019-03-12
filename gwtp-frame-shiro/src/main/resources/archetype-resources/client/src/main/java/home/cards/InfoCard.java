package ${package}.home.cards;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;

public class InfoCard<Ui extends UiHandlers> extends ViewWithUiHandlers<Ui>{

	private static final StyleBundle.Style style = StyleBundle.resources.style();

	protected final Label label = new Label();
	private final FlowPanel cardBodyGroup = new FlowPanel();
	protected final SimplePanel iconPanel = new SimplePanel();
	private final FlowPanel loaderContent = new FlowPanel();
	private final SimplePanel cardPanel = new SimplePanel();

	public Panel getPanel() {
		return cardPanel;
	}

	public InfoCard() {
		cardPanel.setStyleName(style.startInserted());
		SimplePanel infoCard = new SimplePanel();
		cardPanel.add(infoCard);
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
	
	public InfoCard<Ui> updateInfoItems(List<InfoItem> items) {
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
	public InfoCard<Ui> updateRedirect(String title, String link) {
		if(null != cardRedirect) {
			cardRedirect.removeFromParent();
			cardRedirect = null;
		}
		if(null != title && null != link) {
			style.ensureInjected();
			cardRedirect = new SimplePanel();
			loaderContent.add(cardRedirect);
			
			Element anchor = DOM.createAnchor();
			cardRedirect.getElement().appendChild(anchor);
			anchor.setClassName(style.cardRedirect() + " " + style.infoCardItem());
			anchor.setAttribute("href", link);
			Element span = DOM.createSpan();
			Element icon = DOM.createDiv();
			icon.setClassName(style.matIcon());
			span.appendChild(icon);
			icon.appendChild(Icons.arrowIcon());

			anchor.appendChild(span);
			span.setClassName(style.infoCardItemLeftGutter());
			Element p = DOM.createDiv();
			p.setClassName(style.InfoCardItemContent());
			p.setInnerHTML(title);
			anchor.appendChild(p);
		}
		return this;
	}
}
