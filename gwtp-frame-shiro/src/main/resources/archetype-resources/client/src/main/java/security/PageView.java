package ${package}.security;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;
import ${package}.utils.TagWrapBuilder;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	private final StyleBundle.Style style = StyleBundle.resources.style();
	private final FlowPanel listPanel;
	
	@Inject
	public PageView() {
		style.ensureInjected();
		SimplePanel container = new SimplePanel();
		container.setStyleName(style.containerStyle());
		initWidget(container);
		listPanel = new FlowPanel();
		listPanel.addStyleName(style.infoLists());
		container.add(listPanel);
		Element header = Document.get().createElement("header");
		header.addClassName(style.header());
		Element e = Document.get().createHElement(1);
		e.addClassName(style.headerTitle());
		e.setInnerHTML("账户信息");
		header.appendChild(e);
		e = Document.get().createDivElement();
		e.addClassName(style.headerDescript());
		e.setInnerHTML("您在服务中使用的基本信息");
		header.appendChild(e);
		listPanel.getElement().appendChild(header);
	}

	private Panel anchorPanel(String key, String value) {
		style.ensureInjected();
		SimplePanel panel = new SimplePanel();
		panel.addStyleName(style.anchorStyle());
		FlowPanel inside = new FlowPanel();
		panel.add(inside);
		inside.addStyleName(style.anchorStyleInside());
		
		FlowPanel flow = new FlowPanel();
		inside.add(flow);
		flow.addStyleName(style.anchorKeyValue());
		Panel p = new TagWrapBuilder(Icons.arrowIcon(), "figure").addStyleName(style.anchorArrow()).build();
		inside.add(p);

		DivElement keyDiv = Document.get().createDivElement();
		keyDiv.addClassName(style.anchorKey());
		Element e = Document.get().createHElement(3);
		e.addClassName(style.anchorKeyH());
		e.setInnerHTML(key);
		keyDiv.appendChild(e);
		flow.getElement().appendChild(keyDiv);
		e = Document.get().createDivElement();
		e.addClassName(style.anchorValue());
		flow.getElement().appendChild(e);
		Element v = Document.get().createDivElement();
		v.addClassName(style.anchorValueH());
		v.setInnerHTML(value);
		e.appendChild(v);
		
		FlowPanel infoItemPanel = new FlowPanel();
		infoItemPanel.addStyleName(style.infoPice());
		Element separator = Document.get().createDivElement();
		separator.addClassName(style.separator());
		Element border = Document.get().createDivElement();
		border.addClassName(style.separatorBorder());
		separator.appendChild(border);
		infoItemPanel.getElement().appendChild(separator);
		infoItemPanel.add(panel);
		
		infoItemPanel.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().update(key);
			}

		}, ClickEvent.getType());
		return infoItemPanel;
	}
	
	private Panel infoPanel(String infoTitle, Map<String, String> info) {
		style.ensureInjected();
		SimplePanel panel = new SimplePanel();
		panel.addStyleName(style.article());
		FlowPanel articleBody = new FlowPanel();
		articleBody.addStyleName(style.articleBody());
		panel.add(articleBody);
		FlowPanel articleTitle = new FlowPanel();
		articleTitle.addStyleName(style.articleTitle());
		articleBody.add(articleTitle);
		Element e = Document.get().createHElement(2);
		e.addClassName(style.articleTitleHeader());
		e.setInnerHTML(infoTitle);
		articleTitle.getElement().appendChild(e);
		
		for(Entry<String, String> entry:info.entrySet())
			articleBody.add(anchorPanel(entry.getKey(), entry.getValue()));

		return panel;
	}
	
	private Panel personInfo() {
		return infoPanel("个人资料", getUiHandlers().getSecurityInformation());
	}

	private Panel contactInfo() {
		return infoPanel("联系信息", getUiHandlers().getContactInformation());
	}
	
	@Override
	public void showInformation() {
		listPanel.add(personInfo());
		listPanel.add(contactInfo());
	}
}
