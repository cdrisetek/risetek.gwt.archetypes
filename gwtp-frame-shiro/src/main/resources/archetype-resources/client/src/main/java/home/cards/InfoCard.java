package ${package}.home.cards;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;

public class InfoCard<Ui extends UiHandlers> extends ViewWithUiHandlers<Ui>{

	interface MyUiBinder extends UiBinder<HTMLPanel, InfoCard<?>> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyStyle extends CssResource {
		String infoCardItem();
		String startInserted();
		String infoCardItemLeftGutter();
		String infoCardItemContentLeft();
		String InfoCardItemContent();
		String matIcon();
		String cardRedirect();
		String infoTextLine();
		String cardInfoText();
		String cardInfoTextSecondary();
	}
	
	@UiField MyStyle style;
	@UiField public DivElement headLabel;
	@UiField FlowPanel cardBodyGroup;
	@UiField public DivElement iconPanel;
	@UiField FlowPanel loaderContent;

	public InfoCard() {
		initWidget(uiBinder.createAndBindUi(this));
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
			icon.appendChild(new Icons.ArrowRight().getElement());

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
