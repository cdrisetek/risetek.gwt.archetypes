package ${package}.ui.infinitycard;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.view.client.Range;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public abstract class CardInfinityView<E, C extends CardWidget<E>>
                extends ViewWithUiHandlers<CardUiHandlers<E>>
		        implements ResizeHandler, CardPresenterWidget.CardView<E, C> {
	private final int DEFAULT_PIECE_SIZE = 20;
	private final int DEFAULT_PIECE_COUNT = 4;

	interface Bundle extends ClientBundle {
		final Bundle resources = GWT.create(Bundle.class);
		
		interface Style extends CssResource {
			String infinityContainer();
			String cardsContainer();
		}
		
		@Source("style.gss")
		Style style();
	}
	private final Bundle.Style style = Bundle.resources.style();
	
	private final AbsolutePanel absolutePanel = new AbsolutePanel();
	private final FlowPanel infinite = new FlowPanel(), cardsFlowPanel = new FlowPanel();

	public CardInfinityView() {
		style.ensureInjected();
		infinite.setStyleName(style.infinityContainer());
		absolutePanel.add(infinite);
		infinite.add(cardsFlowPanel);
		cardsFlowPanel.setStyleName(style.cardsContainer());
		ResizeLayoutPanel resizePanel = new ResizeLayoutPanel();
		resizePanel.setWidth("100%");
		resizePanel.addResizeHandler(this);
		// We need the resize-event only
		absolutePanel.add(resizePanel);
		initWidget(absolutePanel);
	}

	abstract public IsCardWidget<E> buildCard(E entity);
	
	private int pieceSize = DEFAULT_PIECE_SIZE;
	private int pieceCount = DEFAULT_PIECE_COUNT;
	private int topBarrier = 0, bottomBarrier = 0;
	private volatile int visibleCardIndex = -1, realVisibleTop = 0;
	private volatile boolean allowScroll = true;
	private HandlerRegistration nativePreviewHandlerReg;

	@Override
	protected void onDetach() {
		super.onDetach();
		nativePreviewHandlerReg.removeHandler();
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		nativePreviewHandlerReg = Event.addNativePreviewHandler(event -> {
			int eventType = event.getTypeInt();
			if (eventType != Event.ONMOUSEWHEEL)
				return;
			if(!allowScroll)
				return;

			int mouseY = event.getNativeEvent().getMouseWheelVelocityY();
			double originTop, top = originTop = getDisplayOffsetTop();
			top -= mouseY * 20;

			boolean scrollUp = mouseY < 0;
			// Reset to offset 0 when scroll up and beyond the first card.
			if (scrollUp && top > 0)
				top = 0;
			// Return to old position when scroll down and beyond the last card.
			else if (!scrollUp && (originTop + getScrollHeight() + getCardHeight(getDisplayCardsCount()-1)) < (getRealViewOffsetHeight()*2/3))
				top = originTop;

			// load more data when cross barrier.
			if(scrollUp && (getRealViewOffsetHeight() - top) < topBarrier) {
				// TODO: should show some thing to indicate loading progress?
				int start = entityOffset - pieceSize;
				if(start < 0)
					start = 0;
				int count = entityOffset - start;
				if(count > 0) {
					LoadRange loadRange = new LoadRange();
					loadRange.currentRange = new Range(entityOffset, getDisplayCardsCount());
					loadRange.loadRange = new Range(start, count);
	
					loadRange.originOffset = originTop;
					loadRange.viewHeight = getRealViewOffsetHeight();
					loadRange.scrolledOffset = top;
					allowScroll = false;
					getUiHandlers().onLoadRange(loadRange);
				}
			} else if(!scrollUp && (top + bottomBarrier) < 0) {
				// TODO: should show some thing to indicate loading progress?
				LoadRange loadRange = new LoadRange();
				loadRange.currentRange = new Range(entityOffset, getDisplayCardsCount());
				loadRange.loadRange = new Range(getDisplayCardsCount() + entityOffset, (pieceSize * 1));
				loadRange.originOffset = originTop;
				loadRange.viewHeight = getRealViewOffsetHeight();
				loadRange.scrolledOffset = top;
				
				allowScroll = false;
				getUiHandlers().onLoadRange(loadRange);
			}

			final double deferredTop = top;
			Scheduler.get().scheduleDeferred(()->setDisplayOffsetTop(deferredTop));
		});
	}

	private int getFirstVisibleCardIndex() {
		int count = getDisplayCardsCount();
		if (count <= 0)
			return -1;

		int displayTop = getDisplayOffsetTop();
		int start = 0, end = count - 1, pos = count / 2;

		while (true) {
			if ((getCardOffsetTop(pos) + displayTop) < 0) {
				start = pos;
				pos += (end - pos) / 2;
			} else {
				end = pos;
				if (pos == 0)
					return 0;

				if ((getCardOffsetTop(pos - 1) + displayTop) < 0)
					return pos;
				pos -= (pos - start + 1) / 2;
			}
		}
	}

	private int getDisplayOffsetTop() {
		return infinite.getElement().getOffsetTop();
	}

	private int getScrollHeight() {
		return cardsFlowPanel.getOffsetHeight();
	}

	private void setDisplayOffsetTop(double top) {
		infinite.getElement().getStyle().setTop(top, Style.Unit.PX);
		
		// find the full visible top card and get the value of offset height
		// after set a new Top offset to it's parent.
		visibleCardIndex = getFirstVisibleCardIndex();
		if (visibleCardIndex >= 0)
			realVisibleTop = getCardOffsetTop(visibleCardIndex);
	}

	private int getRowCount() {
		return pieceSize;
	}

	private int getDisplayCardsCount() {
		return cardsFlowPanel.getWidgetCount();
	}

	private int getRealViewOffsetHeight() {
		return absolutePanel.getParent().getOffsetHeight();
	}

	private int getCardOffsetTop(int index) {
		if(0 == getDisplayCardsCount())
			return 0;

		if(getDisplayCardsCount() <= index)
			GWT.log("May be out of index, index: " + index + " total count:" + getDisplayCardsCount());

		assert index < getDisplayCardsCount() 
		       : "Out of index, index: " + index + " total count:" + getDisplayCardsCount();
		return cardsFlowPanel.getWidget(index).getElement().getOffsetTop();
	}

	private int getCardHeight(int index) {
		if(0 == getDisplayCardsCount())
			return 0;

		if(getDisplayCardsCount() <= index)
			GWT.log("May be out of index, index: " + index + " total count:" + getDisplayCardsCount());

		assert index < getDisplayCardsCount()
		       : "Out of index, index: " + index + " total count:" + getDisplayCardsCount();
		return cardsFlowPanel.getWidget(index).getElement().getOffsetHeight();
	}

	private int entityOffset = 0;

	@Override
	public void updateRowData(int start, List<? extends E> values, final LoadRange loadRange) {
		Scheduler.get().scheduleDeferred(()->allowScroll = true);
		if(null == values)
			return;
		if(entityOffset != loadRange.currentRange.getStart() || getDisplayCardsCount() != loadRange.currentRange.getLength()) {
			GWT.log("XXXXX, data changed?");
			return;
		}

		GWT.log("-------- Display top: " + loadRange.originOffset);
		GWT.log(" Current entity begin:" + entityOffset);
		GWT.log(" Request loading from: " + loadRange.loadRange.getStart() + " size:" + loadRange.loadRange.getLength());
		GWT.log("  Response RowData start: " + start + " size:" + values.size());

		if (values.size() == 0) {
			GWT.log("no more data, should we show something?");
			GWT.log("request from:" + loadRange.loadRange.getStart() + " size:" + loadRange.loadRange.getLength());
			return;
		}
		
		boolean isAppend = false;
		if(start > entityOffset + getDisplayCardsCount() || start < entityOffset - values.size()) {
			GWT.log("!!! Error, new data offline. discard");
			return;
		} else if(start >= entityOffset - values.size() + getDisplayCardsCount()) {
			GWT.log(" new data should be append");
			isAppend = true;
		} else if(start <= entityOffset + getDisplayCardsCount()) {
			GWT.log(" new data should be insert");
		} else {
			GWT.log("new data has some issue? discard");
			return;
		}

		// TODO: calculate overlap range;
		Range overlapRange;
		int remove_start = Math.max(start, loadRange.currentRange.getStart());
		int remove_end = Math.min(start + values.size(), loadRange.currentRange.getStart() + loadRange.currentRange.getLength());
		overlapRange = new Range(remove_start, remove_end - remove_start);
		if(overlapRange.getLength() > 0) {
			GWT.log("TODO: overlap from: " + overlapRange.getStart() + " size:" + overlapRange.getLength());

			if(values.size() - overlapRange.getLength() <= 0) {
				GWT.log("no datas to inser or append");
				return;
			}
		}

		int i = 0;
		for (E entity : values) {
			IsCardWidget<E> card = buildCard(entity);
			if(isAppend)
				cardsFlowPanel.add(card);
			else {
				cardsFlowPanel.insert(card, i++);
				entityOffset--;
			}
		}

		// Only remain a reasonable number of cards.
		int removedCardsCount = getDisplayCardsCount()  - pieceCount * getRowCount();
		if(removedCardsCount > 0) {
			int removedCardsHeight = 0;
			if(isAppend)
				removedCardsHeight = getCardOffsetTop(removedCardsCount);
			else
				removedCardsHeight = -getCardOffsetTop(values.size());

			GWT.log(" Remove " + (isAppend? "top ":"bottom ") + removedCardsCount + " cards");
			for (int index = 0; index < removedCardsCount; index++) {
				if (isAppend) {
					cardsFlowPanel.remove(0);
					entityOffset++;
				} else {
					cardsFlowPanel.remove(getDisplayCardsCount() - 1);
				}
			}
		
			setDisplayOffsetTop(getDisplayOffsetTop() + removedCardsHeight);
			GWT.log(" adjust display top: " + removedCardsHeight + "px");
		}
		
		GWT.log(" Entity offset update to: " + entityOffset);
		Scheduler.get().scheduleDeferred(()->{
			// Calculate new barriers.
			// deferred this procedure for the real offset.
			topBarrier = getCardOffsetTop(Math.min(getDisplayCardsCount(), getRowCount() + 1) - 1);
			bottomBarrier = getCardOffsetTop(Math.max(1, (getDisplayCardsCount() - getRowCount())) - 1);
		});
	}

	@Override
	public void refresh() {
		cardsFlowPanel.clear();
		entityOffset = 0;
		setDisplayOffsetTop(0);
		LoadRange loadRange = new LoadRange();
		loadRange.currentRange = new Range(entityOffset, getDisplayCardsCount());
		loadRange.loadRange = new Range(entityOffset, pieceSize * pieceCount);
		loadRange.originOffset = 0;
		loadRange.scrolledOffset = 0;
		loadRange.viewHeight = getRealViewOffsetHeight();
		
		getUiHandlers().onLoadRange(loadRange);
	}

	@Override
	public void onResize(ResizeEvent event) {
		if (visibleCardIndex < 0)
			return;
		int distance = getCardOffsetTop(visibleCardIndex);
		if (distance == realVisibleTop)
			return;

		GWT.log("First full visible card index: " + visibleCardIndex + ", offset height:" + realVisibleTop + "px");
		GWT.log("First Card Distance: " + distance);
		setDisplayOffsetTop(getDisplayOffsetTop() - distance + realVisibleTop);
	}
}
