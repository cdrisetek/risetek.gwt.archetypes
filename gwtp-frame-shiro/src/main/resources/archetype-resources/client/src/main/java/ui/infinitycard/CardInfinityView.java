package ${package}.ui.infinitycard;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public abstract class CardInfinityView<E extends Comparable<E>, C extends CardWidget<E>, H extends CardUiHandlers<E>>
                extends ViewWithUiHandlers<H>
		        implements CardPresenterWidget.CardView<E, H>, ResizeHandler {
	private final int DEFAULT_PIECE_SIZE = 20;
	private final int DEFAULT_PIECE_COUNT = 4;
	private final int MOUSEY_DELTA_FACTOR = 20;

	@UiField ResizeLayoutPanel resizePanel;
	// Slider，一个像滑片一样的Panel，通过改变这个Panel的顶部位置实现上下滑动，从而达到可视区域显示需要信息的效果。 
	@UiField DivElement panelSilder;
	@UiField FlowPanel panelCards;
	
	// 可视区域的高度
	private int viewWindowHeight = 0;
	
	interface Binder extends UiBinder<Widget, CardInfinityView<?, ?, ?>> {
		Binder binder = GWT.create(Binder.class);
	}

	public CardInfinityView() {
		initWidget(Binder.binder.createAndBindUi(this));
		resizePanel.addResizeHandler(this);
	}

	abstract public CardWidget<E> buildCard(E entity);

	private int pieceSize = DEFAULT_PIECE_SIZE;
	private int pieceCount = DEFAULT_PIECE_COUNT;
	private int topBarrier = 0, bottomBarrier = 0;
	/* backup position when slider top changed, this help locate first card when response resize event */
	private volatile int visibleCardIndex = -1, lastVisibleTop = 0;
	private volatile boolean scrollable = true;
	private HandlerRegistration nativePreviewHandlerReg;

	@Override
	protected void onDetach() {
		super.onDetach();
		nativePreviewHandlerReg.removeHandler();
	}

	private long datasequence = 0, latestsequence = -1;
	private void onLoadRange(double originTop, double newTop, Range dataRange) {
		scrollable = false;

		LoadRange loadRange = new LoadRange();
		loadRange.currentRange = new Range(entityOffset, getTotalCardsCount());
		loadRange.loadRange = dataRange;
		loadRange.originOffset = originTop;
		loadRange.viewHeight = viewWindowHeight;
		loadRange.scrolledOffset = newTop;
		loadRange.datasequence = ++datasequence;
		// TODO: should show some thing to indicate loading progress?
		GWT.log("loading sequence: " + loadRange.datasequence);
		GWT.log("DEBUG: orgin: " + originTop);
		GWT.log("DEBUG: new top: " + newTop);
		getUiHandlers().onLoadRange(loadRange);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();

		nativePreviewHandlerReg = Event.addNativePreviewHandler(event -> {
			int eventType = event.getTypeInt();
			if (eventType != Event.ONMOUSEWHEEL)
				return;
			if(!scrollable)
				return;

			int mouseY = event.getNativeEvent().getMouseWheelVelocityY();
			double originTop, top = originTop = getSliderTop();
			top -= mouseY * MOUSEY_DELTA_FACTOR;

			boolean scrollUp = mouseY < 0;
			// Reset to offset 0 when scroll up and beyond the first card.
			if (scrollUp && top > 0 /* had beyond the top */)
				top = 0;
			// Return to old position when scroll down and beyond the last card.
			else if (!scrollUp && isBeyondBottom(originTop))
				top = originTop;

			// load more data when cross barrier.
			if(scrollUp && (viewWindowHeight - top) < topBarrier) {
				int start = entityOffset - pieceSize;
				if(start < 0)
					start = 0;
				int count = entityOffset - start;
				if(count > 0)
					onLoadRange(originTop, top, new Range(start, count) /* data range */);
			} else if(!scrollUp && (top + bottomBarrier) < 0) {
				onLoadRange(originTop, top, new Range(getTotalCardsCount() + entityOffset, (pieceSize * 1)) /* data range */);
			}

			final double deferredTop = top;
			Scheduler.get().scheduleDeferred(() -> {
				setSliderPanelTop(deferredTop);
				checkVisible();
			});
		});
	}

	// 查找可视区域的第一张卡片
	private int getFirstVisibleCardIndex() {
		int count = getTotalCardsCount();
		if (count <= 0)
			return -1;

		int displayTop = getSliderTop();
		int start = 0, end = count - 1, pos = count / 2;

		while (true) {
			if ((getCardTop(pos) + displayTop) < 0) {
				start = pos;
				pos += (end - pos) / 2;
			} else {
				end = pos;
				if (pos == 0)
					return 0;

				if ((getCardTop(pos - 1) + displayTop) < 0)
					return pos;
				pos -= (pos - start + 1) / 2;
			}
		}
	}

	// 确定卡片是否在显示窗口内。
	private boolean isVisible(int offset, int windowHeight, CardWidget<E> card) {
		Element e = card.getElement();
		int cardHeight = e.getOffsetHeight();
		int cardTop = e.getOffsetTop();
		
		if(((cardTop + offset) < windowHeight) && ((cardTop + cardHeight + offset) > 0))
			return true;

		return false;
	}
	
	private void checkVisible() {
		// Slider TOP发生变化，意味着曾经显示的卡片可能被移出窗口区，或者未显示的卡片显示出来了，我们需要调用onShow, onHide。
		panelCards.iterator().forEachRemaining( w -> {
			@SuppressWarnings("unchecked")
			CardWidget<E> card = (CardWidget<E>)w;
			if(isVisible(getSliderTop(), viewWindowHeight, card)) {
				if(!card.visible)
					card.onShow();
				card.visible = true;
			} else {
				if(card.visible)
					card.onHide();
				card.visible = false;
			}
		});
	}

	private int getSliderTop() {
		return panelSilder.getOffsetTop();
	}

	private int getCardsPanelHeight() {
		return panelCards.getOffsetHeight();
	}

	private void setSliderPanelTop(double top) {
		panelSilder.getStyle().setTop(top, Style.Unit.PX);
		
		// find the full visible top card and get the value of offset height
		// after set a new Top offset to it's parent.
		visibleCardIndex = getFirstVisibleCardIndex();
		if (visibleCardIndex >= 0)
			lastVisibleTop = getCardTop(visibleCardIndex);
	}

	private int getRowCount() {
		return pieceSize;
	}

	private int getTotalCardsCount() {
		return panelCards.getWidgetCount();
	}

	private int getCardTop(int index) {
		if(0 == getTotalCardsCount())
			return 0;

		if(getTotalCardsCount() <= index)
			GWT.log("May be out of index, index: " + index + " total count:" + getTotalCardsCount());

		assert index < getTotalCardsCount() 
		       : "Out of index, index: " + index + " total count:" + getTotalCardsCount();
		return panelCards.getWidget(index).getElement().getOffsetTop();
	}

	private boolean isBeyondBottom(double originTop) {
		return ((originTop + getCardsPanelHeight() + getCardHeight(-1) /* last card height */) < (viewWindowHeight*2/3));
	}
	private int getCardHeight(int index) {
		// process revert index.
		if(index < 0)
			index += getTotalCardsCount();

		if(0 == getTotalCardsCount())
			return 0;

		if(getTotalCardsCount() <= index)
			GWT.log("May be out of index, index: " + index + " total count:" + getTotalCardsCount());

		assert index < getTotalCardsCount()
		       : "Out of index, index: " + index + " total count:" + getTotalCardsCount();
		return panelCards.getWidget(index).getElement().getOffsetHeight();
	}

	// 数据开始的偏移值
	private int entityOffset = 0;
	// TODO: 如果数据中间会发生变化，或者最开始的数据本身也存在新数据成为开始数据，处理过程就要复杂得多。

	@Override
	public void updateRowData(int start, List<? extends E> values, final LoadRange loadRange) {
		Scheduler.get().scheduleDeferred(() -> scrollable = true);

		if(latestsequence > loadRange.datasequence) {
			GWT.log("delayed data sequence");
			return;
		}
		latestsequence = loadRange.datasequence;

		if(loadRange.datasequence != datasequence)
			GWT.log("DEBUG: datasequence request: " + datasequence + " response: " + loadRange.datasequence);
		else
			GWT.log("DEBUG: datasequence matched: " + datasequence);

		if(null == values)
			return;
		if(entityOffset != loadRange.currentRange.getStart() || getTotalCardsCount() != loadRange.currentRange.getLength()) {
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
		if(start > entityOffset + getTotalCardsCount() || start < entityOffset - values.size()) {
			GWT.log("!!! Error, new data offline. discard");
			return;
		} else if(start >= entityOffset - values.size() + getTotalCardsCount()) {
			GWT.log(" new data should be append");
			isAppend = true;
		} else if(start <= entityOffset + getTotalCardsCount()) {
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
			CardWidget<E> card = buildCard(entity);
			if(isAppend)
				panelCards.add(card);
			else {
				panelCards.insert(card, i++);
				entityOffset--;
			}
		}

		// Only remain a reasonable size of cards.
		// TODO: 如果需要移除的卡片还没显示，那么高度计算存在问题。
		int removedCardsCount = getTotalCardsCount()  - pieceCount * getRowCount();
		if(removedCardsCount > 0) {
			int removedCardsHeight = 0;
			if(isAppend)
				removedCardsHeight = getCardTop(removedCardsCount);
			else
				removedCardsHeight = -getCardTop(values.size());

			GWT.log(" Remove " + (isAppend? "top ":"bottom ") + removedCardsCount + " cards");
			for (int index = 0; index < removedCardsCount; index++) {
				if (isAppend) {
					panelCards.remove(0);
					entityOffset++;
				} else {
					panelCards.remove(getTotalCardsCount() - 1);
				}
			}
		
			setSliderPanelTop(getSliderTop() + removedCardsHeight);
			GWT.log(" adjust display top: " + removedCardsHeight + "px");
		}
		
		GWT.log(" Entity offset update to: " + entityOffset);
		Scheduler.get().scheduleDeferred(() -> {
			// Calculate new barriers.
			// deferred this procedure for the real offset.
			topBarrier = getCardTop(Math.min(getTotalCardsCount(), getRowCount() + 1) - 1);
			bottomBarrier = getCardTop(Math.max(1, (getTotalCardsCount() - getRowCount())) - 1);
			checkVisible();
		});
	}

	@Override
	public void refresh() {
		panelCards.clear();
		entityOffset = 0;
		setSliderPanelTop(0);
		GWT.log("refresh");
		onLoadRange(0, 0, new Range(entityOffset, pieceSize * pieceCount));
	}

	@Override
	public void onResize(ResizeEvent event) {
		// Update View Window size;
		viewWindowHeight = panelSilder.getParentElement().getParentElement().getOffsetHeight();
		if (visibleCardIndex < 0)
			return;
		
		// 如果窗口宽度发生变化，Slider内的卡片会响应这个变化，从而产生高度的变化。
		// 为了保持第一个可视卡片的位置固定，需要计算并重新设定Slider的top，以响应这个变化。
		int distance = getCardTop(visibleCardIndex);
		if (distance == lastVisibleTop)
			return;

		GWT.log("onResize First card index: " + visibleCardIndex + ", offset height:"
				+ lastVisibleTop + "px" + ", First Card Distance: " + distance);
		setSliderPanelTop(getSliderTop() - distance + lastVisibleTop);
		checkVisible();
	}

	@SuppressWarnings("unchecked")
	public void iteratorCards(Consumer<CardWidget<E>> consumer) {
		panelCards.iterator().forEachRemaining( w -> consumer.accept((CardWidget<E>)w));
	}
}
