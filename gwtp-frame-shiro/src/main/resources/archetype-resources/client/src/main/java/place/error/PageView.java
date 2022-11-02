package ${package}.place.error;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@UiField HTMLPanel label;
	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@Override
	public void setErrorMessage(String message) {
		label.getElement().setInnerHTML(message);
	}
	
}
