package ${package}.place.root;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;

public class PageView extends ViewWithUiHandlers<MyUiHandlers> implements RootPresenter.MyView {
	interface Binder extends UiBinder<HTMLPanel, PageView> {}

    @UiField
	SimplePanel mainContainer, topContainer;

    @Inject
    public PageView(Binder binder) {
    	initWidget(binder.createAndBindUi(this));
		asWidget().getElement().setAttribute("layout", "column");
        mainContainer.getElement().setAttribute("layout", "row");
    }

	@Override
	public void bindMainSlot(NestedSlot slot) {
		bindSlot(slot, mainContainer);
	}


	@Override
	public void bindMenuSlot(NestedSlot slot) {
		bindSlot(slot, topContainer);
	}


	@Override
	public void bindLeftSlot(NestedSlot slot) {
		// Not yet
	}
}
