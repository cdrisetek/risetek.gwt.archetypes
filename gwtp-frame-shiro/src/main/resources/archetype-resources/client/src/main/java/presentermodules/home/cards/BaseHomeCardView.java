package ${package}.presentermodules.home.cards;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;

public class BaseHomeCardView<Ui extends UiHandlers> extends ViewWithUiHandlers<Ui> implements IHomeCardView {

	interface MyUiBinder extends UiBinder<Widget, BaseHomeCardView<?>> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyStyle extends CssResource {
		String homeCardItem();
		String infoCardItemLeftGutter();
		String infoCardItemContentLeft();
		String InfoCardItemContent();
		String homeCardRedirect();
		String infoTextLine();
		String cardInfoText();
		String cardInfoTextSecondary();
		String homeCardAction();
	}
	
	@UiField MyStyle style;
	@UiField public DivElement headLabel;
	@UiField FlowPanel cardBodyGroup, actionGroup;
	@UiField public DivElement iconPanel;
	@UiField FlowPanel loaderContent;

	public BaseHomeCardView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public BaseHomeCardView<Ui> updateInfoItems(List<InfoItem> items) {

		for(InfoItem item:items) {
			FlowPanel itemPanel = new FlowPanel();
			itemPanel.addStyleName(style.homeCardItem());
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
				for(String s : item.infoTextSecondary) {
					Element textLine = DOM.createDiv();
					textLine.setClassName(style.infoTextLine());
					Label text = new Label(s);
					text.setStyleName(style.cardInfoText());
					text.addStyleName(style.cardInfoTextSecondary());
					textLine.appendChild(text.getElement());
					infoCardContentLeft.getElement().appendChild(textLine);
				}
			}
			cardBodyGroup.add(itemPanel);
		}
		return this;
	}

	public void addAction(String title, ClickHandler handler) {
		style.ensureInjected();
		FlowPanel action = new FlowPanel();
		action.setStyleName(style.homeCardAction());
		action.addDomHandler(handler, ClickEvent.getType());
		actionGroup.add(action);
		
		SimplePanel iconwrap = new SimplePanel();
		iconwrap.addStyleName(style.infoCardItemLeftGutter());
		
		SimplePanel icon = new SimplePanel();
		icon.addStyleName("rt-icon-s24");
		icon.add(new Icons.ArrowRight());
		
		iconwrap.add(icon);
		action.add(iconwrap);
		
		SimplePanel contextwrap = new SimplePanel();
		contextwrap.setStyleName(style.InfoCardItemContent());
		SimplePanel context = new SimplePanel();
		context.setStyleName(style.homeCardItem());
		context.getElement().setInnerHTML(title);
		contextwrap.add(context);
		action.add(contextwrap);
	}
	
	@Override
	public void clear() {
		cardBodyGroup.clear();
		actionGroup.clear();
	}
	
	public BaseHomeCardView<Ui> setTitle(String title, Element icon) {
		headLabel.setInnerHTML(title);
		iconPanel.appendChild(icon);
		return this;
	}
	
	public BaseHomeCardView<Ui> setTitle(String title, Widget icon) {
		return setTitle(title, icon.getElement());
	}
}
