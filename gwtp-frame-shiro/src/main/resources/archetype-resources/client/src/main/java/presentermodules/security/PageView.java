package ${package}.presentermodules.security;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.utils.Icons;
import ${package}.utils.TagWrapBuilder;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	interface MyStyle extends CssResource {
		String anchorStyle();
		String anchorStyleInside();
		String anchorKeyValue();
		String anchorArrow();
		String anchorKey();
		String anchorKeyH();
		String anchorValue();
		String anchorValueH();
		String infoPice();
		String article();
		String articleBody();
		String articleTitle();
		String articleTitleHeader();
		String separator();
		String separatorBorder();
	}
	
	interface MyUiBinder extends UiBinder<HTMLPanel, PageView> {}
	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField MyStyle style;
	@UiField Panel listPanel;
	
	@Inject
	public PageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Panel anchorPanel(String key, Map<String, String> value) {
		style.ensureInjected();
		SimplePanel panel = new SimplePanel();
		panel.addStyleName(style.anchorStyle());
		FlowPanel inside = new FlowPanel();
		panel.add(inside);
		inside.addStyleName(style.anchorStyleInside());
		
		FlowPanel flow = new FlowPanel();
		inside.add(flow);
		flow.addStyleName(style.anchorKeyValue());
		
		if(value.get("link") != null) {
			Panel p = new TagWrapBuilder(new Icons.ArrowRight(), "figure").addStyleName(style.anchorArrow()).build();
			inside.add(p);
		}

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
		v.setInnerHTML(value.get("value"));
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
				getUiHandlers().update(value.get("link"));
			}

		}, ClickEvent.getType());
		return infoItemPanel;
	}
	
	private Panel infoPanel(String infoTitle, Map<String, Map<String, String>> info) {
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
		
		for(Entry<String, Map<String, String>> entry:info.entrySet())
			articleBody.add(anchorPanel(entry.getKey(), entry.getValue()));

		return panel;
	}
	
	@Override
	public void onAttach() {
		showInformation();
	}
	
	@Override
	public void showInformation() {
		listPanel.clear();
		listPanel.add(infoPanel("个人资料", getUiHandlers().getSecurityInformation()));
		listPanel.add(infoPanel("联系信息", getUiHandlers().getContactInformation()));
	}
}
