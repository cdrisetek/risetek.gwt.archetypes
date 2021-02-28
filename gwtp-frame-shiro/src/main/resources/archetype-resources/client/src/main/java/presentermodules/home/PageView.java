package ${package}.presentermodules.home;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.presenter.slots.Slot;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	@UiField HTMLPanel container;
	private final int columnSize = 3;
	private final FlowPanel[] column = new FlowPanel[columnSize];

	interface Binder extends UiBinder<Widget, PageView> {}
	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		for(int i=0; i<columnSize; i++) {
			column[i] = new FlowPanel();
			container.add(column[i]);
		}
	}

	@Override
	public void bindSlot(Slot<?> slot, int index) {
		bindSlot(slot, column[index]);
	}

	@Override
	public int getCloumnSize() {
		return columnSize;
	}
}
