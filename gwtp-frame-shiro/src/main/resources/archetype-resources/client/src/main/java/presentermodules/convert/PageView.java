package ${package}.presentermodules.convert;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	@UiField
	TextArea inputArea;
	@UiField
	SimplePanel translation;

	interface Binder extends UiBinder<Widget, PageView> {}

	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		inputArea.getElement().setAttribute("row", "1");
		inputArea.getElement().setAttribute("spellcheck", "false");
		inputArea.getElement().setAttribute("autocapitalize", "off");
		inputArea.getElement().setAttribute("autocomplete", "off");
		inputArea.getElement().setAttribute("autocorrect", "off");
		inputArea.addKeyUpHandler(c->{getUiHandlers().InputChanged(inputArea.getText());});
	}
	
	@Override
	public void showTranslation(String text) {
		translation.getElement().setInnerText(text);
	}
    protected void onAttach() {
		inputArea.setFocus(true);
    }
}
