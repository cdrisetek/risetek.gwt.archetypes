package ${package}.presentermodules.convert;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	private final StyleBundle.Style style = StyleBundle.resources.style();
	private final TextArea inputArea = new TextArea();
	private final Element translation = DOM.createSpan();
	@Inject
	public PageView() {
		style.ensureInjected();
		SimplePanel contrainer = new SimplePanel();
		initWidget(contrainer);
		contrainer.setStyleName(style.pageStyle());
		FlowPanel frame = new FlowPanel();
		frame.setStyleName(style.frame());
		contrainer.add(frame);
		FlowPanel contentWrap = new FlowPanel();
		contentWrap.setStyleName(style.pageContentWrap());
		frame.add(contentWrap);
		
		FlowPanel mainHeader = new FlowPanel();
		mainHeader.setStyleName(style.mainHeader());
		contentWrap.add(mainHeader);
		
		FlowPanel sourceTargetRow = new FlowPanel();
		sourceTargetRow.setStyleName(style.sourceTargetRow());
		mainHeader.add(sourceTargetRow);
		
		FlowPanel sourceWindow = new FlowPanel();
		sourceWindow.setStyleName(style.convertWindow());
		sourceTargetRow.add(sourceWindow);
		inputArea.getElement().setAttribute("row", "1");
		inputArea.getElement().setAttribute("spellcheck", "false");
		inputArea.getElement().setAttribute("autocapitalize", "off");
		inputArea.getElement().setAttribute("autocomplete", "off");
		inputArea.getElement().setAttribute("autocorrect", "off");
		inputArea.setStyleName(style.textArea());
		sourceWindow.add(inputArea);
		Label sourceLabel = new Label("\u6c49\u5b57");
		sourceLabel.setStyleName(style.targetNote());
		sourceWindow.add(sourceLabel);

		FlowPanel targetWindow = new FlowPanel();
		targetWindow.setStyleName(style.convertWindow());
		SimplePanel translationWrap = new SimplePanel();
		translationWrap.setStyleName(style.translation());
		
		translationWrap.getElement().appendChild(translation);
		translation.setAttribute("title", null);
		translation.setPropertyString("contenteditable", "false");
		translation.setTabIndex(-1);

		targetWindow.add(translationWrap);
		Label targetLabel = new Label("UTF-8\u7f16\u7801");
		targetLabel.setStyleName(style.targetNote());
		targetWindow.add(targetLabel);
		SimplePanel middle = new SimplePanel();
		middle.setWidth("4px");
		middle.setHeight("100%");
		sourceTargetRow.add(middle);
		sourceTargetRow.add(targetWindow);
		inputArea.addKeyUpHandler(c->{getUiHandlers().InputChanged(inputArea.getText());});
	}
	
	@Override
	public void showTranslation(String text) {
		translation.setInnerText(text);
	}
    protected void onAttach() {
		inputArea.setFocus(true);
    }
}
