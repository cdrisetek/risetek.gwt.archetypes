package ${package}.presentermodules.home;

import java.util.List;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.presentermodules.home.cards.AbstractHomeCardPresenter;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView, ResizeHandler {

	@UiField HTMLPanel container;
	@UiField ResizeLayoutPanel resizePanel;
	private int columnSize = 0;
	private FlowPanel[] column;
	
	// TODO: where to set homeCard column width?
	private int calculateColumnSize() {
		int cols = resizePanel.getOffsetWidth() / 400 /* column width in px */; 
		return cols > 0 ? cols:1;
	}

	interface Binder extends UiBinder<Widget, PageView> {}
	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		resizePanel.addResizeHandler(this);
	}

	@Override
	public void onResize(ResizeEvent event) {
		if(calculateColumnSize() == columnSize)
			return;

		getUiHandlers().layoutCards();
	}
	
	@Override
	public void onAttach() {
		getUiHandlers().layoutCards();
	}

	@Override
	public void layoutHomeCards(List<AbstractHomeCardPresenter<?, ?>> sortedList) {
		if(null != column) {
			for(FlowPanel c : column)
				c.removeFromParent();
		}

		columnSize = calculateColumnSize();
		column = new FlowPanel[columnSize];
		for(int i=0; i<columnSize; i++) {
			column[i] = new FlowPanel();
			container.add(column[i]);
		}

		for(Presenter<?, ?> p:sortedList) {
			FlowPanel flowPanel = null;
			for(int i = 0; i < column.length; i++) {
				if(flowPanel == null || flowPanel.getWidgetCount() > column[i].getWidgetCount()) {
					flowPanel = column[i];
				}
			}
			if(null != flowPanel) {
				flowPanel.add(p);
			}
		}
	}
}
