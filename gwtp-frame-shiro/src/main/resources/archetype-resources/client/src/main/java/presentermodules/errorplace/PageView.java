package ${package}.presentermodules.errorplace;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
}
