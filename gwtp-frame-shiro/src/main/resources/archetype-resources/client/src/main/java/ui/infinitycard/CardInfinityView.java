package ${package}.ui.infinitycard;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialTitle;

public abstract class CardInfinityView<E, C extends CardWidget<E>> extends ViewWithUiHandlers<CardUiHandlers<E>>
		implements ResizeHandler, CardPresenterWidget.CardView<E, C> {
	private final int DEFAULT_PIECE_SIZE = 20;
	private final int DEFAULT_PIECE_COUNT = 4;

	@UiTemplate("DialogView.ui.xml")
	interface Binder extends UiBinder<MaterialDialog, CardDialog<?>> {
		final Binder uiBinder = GWT.create(Binder.class);
	}

	protected static class CardDialog<E> extends ViewWithUiHandlers<DialogUiHandlers<E>> {
		@UiField MaterialDialog dgPanel;
		@UiField MaterialTitle title;
		@UiField MaterialDialogContent contentContainer;
		@UiField MaterialButton btnEnable, btnDisable, btnDelete;

		private IsCardWidget<E> card;
		CardDialog() {
			Binder.uiBinder.createAndBindUi(this);
			btnEnable.setDisplay(Display.NONE);
			btnDisable.setDisplay(Display.NONE);
		}

		@UiHandler("btnCancel")
		void onCancelClick(ClickEvent e) {
			dgPanel.close();
		}
		
		@UiHandler("btnDelete")
		void onDeleteClick(ClickEvent e) {
			dgPanel.close();
			card.deleteCard();
		}

		@UiHandler("btnCommit")
		void onCommitClick(ClickEvent e) {
			if(!getUiHandlers().checkCommitValid(card))
				return;
			getUiHandlers().onEditorCommit(card);
			dgPanel.close();
		}

		void show(IsWidget content, IsCardWidget<E> card) {
			this.card = card;
			contentContainer.clear();
			contentContainer.add(content);
			title.clearErrorText();
			title.setTitle(getUiHandlers().getDialogTitle(card));
			btnDelete.setDisplay((null == card)?Display.NONE:Display.BLOCK);
			dgPanel.open();
		}
	}
	
	final CardDialog<E> cardDialog = new CardDialog<E>();
	public void setDialogUiHandlers(DialogUiHandlers<E> uiHandlers) {
		cardDialog.setUiHandlers(uiHandlers);
	}
	
	@Override
	public void showDialog(IsCardWidget<E> card) {
		cardDialog.show(getDialogContent(card), card);
	}

	private final AbsolutePanel absolutePanel = new AbsolutePanel();
	private final FlowPanel infinite = new FlowPanel(), cardList = new FlowPanel();
	@Inject
	public CardInfinityView() {
		infinite.getElement().getStyle().setDisplay(com.google.gwt.dom.client.Style.Display.BLOCK);
		infinite.getElement().getStyle().setPosition(Position.RELATIVE);
		absolutePanel.add(infinite);
		infinite.add(cardList);

		ResizeLayoutPanel resizePanel = new ResizeLayoutPanel();
		resizePanel.setWidth("100%");
		resizePanel.addResizeHandler(this);
		absolutePanel.add(resizePanel);
		absolutePanel.add(cardDialog.dgPanel);

		initWidget(absolutePanel);
	}

	abstract public IsWidget getDialogContent(IsCardWidget<E> card);
	abstract public IsCardWidget<E> BuildCard(E entity);
	
	private int pieceSize = DEFAULT_PIECE_SIZE;
	private int pieceCount = DEFAULT_PIECE_COUNT;
	private int topBarrier = 0, bottomBarrier = 0;
	private int realVisibleCardIndex = -1, realVisibleTop = 0;

	private HandlerRegistration nativePreviewHandlerReg;

	@Override
	protected void onDetach() {
		super.onDetach();
		nativePreviewHandlerReg.removeHandler();
	}

/*
	private class DeferredTopCommand implements ScheduledCommand {
		private double pos;

		public DeferredTopCommand(double pos) {
			this.pos = pos;
		}

		@Override
		public void execute() {
			setDisplayOffsetTop(pos);
		}

	}
*/

	private boolean allowScroll = true;
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
			double oldTop, top = oldTop = getDisplayOffsetTop();
			top -= mouseY * 20;
			//double oldTop = getDisplayOffsetTop();
			boolean up = mouseY < 0;
			// Reset to offset 0 when scroll up and beyond the first card.
			if (up && top > 0)
				top = 0;
			// return to old position when scroll down and beyond the last card.
			if (!up && (oldTop + getScrollHeight()) < getRealViewHeight())
				top = oldTop;

			// load more data when cross barrier.
			if ((up && (getRealViewHeight() - top) < topBarrier) || (!up && (top + bottomBarrier) < 0)) {
				// TODO: should show some thing to indicate loading progress?
				allowScroll = false;
				RangeChangeEvent.fire(this, null);
			}

			final double ftop = top;
			Scheduler.get().scheduleDeferred(()->setDisplayOffsetTop(ftop));
			//Scheduler.get().scheduleDeferred(new DeferredTopCommand(top));
		});
	}

	private int getFirstCardIndex() {
		int count = getDisplayCardsCount();
		if (count <= 0)
			return -1;

		int top = getDisplayOffsetTop();
		int start = 0, end = count - 1, pos = count / 2;

		while (true) {
			// GWT.log("index:" + pos + " top at:" + (getCardOffsetTop(pos) + top));

			if ((getCardOffsetTop(pos) + top) < 0) {
				start = pos;
				pos += (end - pos) / 2;
			} else {
				end = pos;
				if (pos == 0)
					return 0;

				if ((getCardOffsetTop(pos - 1) + top) < 0)
					return pos;
				pos -= (pos - start + 1) / 2;
			}
		}
	}

	private void addCard(E entity, int index) {
		IsCardWidget<E> card = BuildCard(entity);
		if (index >= 0)
			cardList.insert(card, index);
		else
			cardList.add(card);
	}

	private int getDisplayOffsetTop() {
		return infinite.getElement().getOffsetTop();
	}

	private int getScrollHeight() {
		return cardList.getOffsetHeight();
	}

	private void setDisplayOffsetTop(double top) {
		infinite.getElement().getStyle().setTop(top, Style.Unit.PX);
		realVisibleCardIndex = getFirstCardIndex();
		if (realVisibleCardIndex >= 0)
			realVisibleTop = getCardOffsetTop(realVisibleCardIndex);
		/*
		 * int firstIndex = getFirstCardIndex(); if(firstIndex < 0) return; int distance
		 * = getCardOffsetTop(firstIndex) + (int)top; GWT.log("First Card:" +
		 * (firstIndex + entityOffset) + " distance: " + distance);
		 */
	}

	private int getDisplayCardsCount() {
		return cardList.getWidgetCount();
	}

	private int getRealViewHeight() {
		return absolutePanel.getParent().getOffsetHeight();
	}

	private int getCardOffsetTop(int index) {
		if(0 == getDisplayCardsCount())
			return 0;

		if(cardList.getWidgetCount() < index)
			GWT.log("May be out of index, index: " + index + " total count:" + cardList.getWidgetCount());
		return cardList.getWidget(index).getElement().getOffsetTop();
	}
/*
	private int getCardHeight(int index) {
		return cardContainer.getWidget(index).getOffsetHeight();
	}
*/
	private int entityOffset = 0;

	// 寻找第一个Card在real view区域的piece index.
	// return from 0 to pieceCount - 1
	// 如果没有card显示，返回0
	private int getVisiblePieceRange() {
		int top = getDisplayOffsetTop();
		int count = getDisplayCardsCount();

		int pCount = pieceCount;
		while (--pCount > 0) {
			int sampleIndex = pCount * getRowCount();
			if (count <= sampleIndex + 1)
				continue;

			Element pieceBottomElement = cardList.getWidget(sampleIndex).getElement();
			int offset = pieceBottomElement.getOffsetTop() + top;
			if (offset < getRealViewHeight())
				return pCount;
		}

		return 0;
	}

	// 已经载入display的是一个连续的区域，从entityOffset开始，载入的数量是getDisplayCardsCount()
	// 我们总是试图装满display，满载的cards数量是pieceCount*getRowCount()
	// Calculate Range for new piece.
	@Override
	public Range getVisibleRange() {
		int count = getDisplayCardsCount();
		int maxLoadSize = pieceCount * getRowCount() - count;
		if (maxLoadSize < 0)
			maxLoadSize = 0;

		int piece = getVisiblePieceRange();

		if (piece <= 1 && (entityOffset > 0)) {
			int newoffset = entityOffset - getRowCount();
			if (newoffset < 0)
				newoffset = 0;
			return new Range(newoffset, (entityOffset - newoffset));
		} else if (piece == pieceCount - 1) {
			return new Range((count + entityOffset), getRowCount());
		} else if (piece == 0 && entityOffset == 0) {
			return new Range(0, maxLoadSize);
		}
		// load nothing
		GWT.log("load nothing");
		return new Range(0, 0);
	}

	@Override
	public void setRowData(int start, List<? extends E> values) {
		Scheduler.get().scheduleDeferred(()->allowScroll = true);
		/*
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				allowScroll = true;
			}
		});
		*/

		if (values.size() == 0)
			return;

		boolean isAppend = (start >= entityOffset);

		if (!isAppend)
			assert (start + values.size() == entityOffset);

		GWT.log("Display add RowData start: " + start + " size:" + values.size() + ", current entity begin:"
				+ entityOffset);

		int countRemove = (getDisplayCardsCount() + values.size()) - pieceCount * getRowCount();

		int deltaTop = 0;
		if (countRemove > 0 && isAppend)
			deltaTop = cardList.getWidget(countRemove).getElement().getOffsetTop();

		if (!isAppend) {
			int i = 0;
			for (E entity : values) {
				addCard(entity, i++);
				entityOffset--;
			}

			// TODO: how about carContainer only have values.size element?
			deltaTop = -cardList.getWidget(values.size()).getElement().getOffsetTop();
		} else {
			for (E entity : values)
				addCard(entity, -1);
		}

		if (countRemove > 0) {
			for (int index = 0; index < countRemove; index++) {
				if (isAppend) {
					cardList.remove(0);
					entityOffset++;
				} else {
					cardList.remove(getDisplayCardsCount() - 1);
				}
			}
		}

		setDisplayOffsetTop(getDisplayOffsetTop() + deltaTop);
		// Set up barriers. Call scheduler deferred for the real offset.
		Scheduler.get().scheduleDeferred(()->{
			topBarrier = getCardOffsetTop(Math.min(getDisplayCardsCount(), getRowCount() + 1) - 1);
			bottomBarrier = getCardOffsetTop(Math.max(1, (getDisplayCardsCount() - getRowCount())) - 1);
		});
		/*
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				topBarrier = getCardOffsetTop(Math.min(getDisplayCardsCount(), getRowCount() + 1) - 1);
				bottomBarrier = getCardOffsetTop(Math.max(1, (getDisplayCardsCount() - getRowCount())) - 1);
			}
		});
		*/
	}

	@Override
	public HandlerRegistration addRangeChangeHandler(Handler handler) {
		return asWidget().addHandler(handler, RangeChangeEvent.getType());
	}

	@Override
	public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
		return asWidget().addHandler(handler, RowCountChangeEvent.getType());
	}

	@Override
	public HandlerRegistration addCellPreviewHandler(CellPreviewEvent.Handler<E> handler) {
		return asWidget().addHandler(handler, CellPreviewEvent.getType());
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		asWidget().fireEvent(event);
	}
	
	@Override
	public int getRowCount() {
		return pieceSize;
	}

	@Override
	public boolean isRowCountExact() {
		return false;
	}

	@Override
	public void setRowCount(int count) {
		pieceSize = count;
	}

	@Override
	public void setRowCount(int count, boolean isExact) {
	}

	@Override
	public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
		entityOffset = 0;
		setDisplayOffsetTop(0);
		cardList.clear();
		if (forceRangeChangeEvent)
			RangeChangeEvent.fire(this, null);
	}

	@Override
	public void onResize(ResizeEvent event) {
		if (realVisibleCardIndex < 0)
			return;
		int distance = getCardOffsetTop(realVisibleCardIndex);
		if (distance == realVisibleTop)
			return;
		/*
		 * GWT.log("Last First Card:" + realVisibleCardIndex + " last distance:" +
		 * realVisibleTop); GWT.log("First Card Distance: " + distance);
		 */
		setDisplayOffsetTop(getDisplayOffsetTop() - distance + realVisibleTop);
	}

	@Override
	public void onCardRemove(IsWidget widget) {
		Scheduler.get().scheduleFixedDelay(()->{cardList.remove(widget); return false;}, 300);
/*		
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			public boolean execute() {
				cardContainer.remove(widget);
				return false;
			}
		}, 300);
*/
	}

	@Override
	public void onCardUpdate(IsCardWidget<E> widget) {
		widget.updateCard(widget.getEntity());
	}
	
	@Override
	public void setVisibleRange(int start, int length) {
		GWT.log("setVisibleRange");
	}

	@Override
	public void setVisibleRange(Range range) {
		GWT.log("setVisibleRange2");
	}

	@Override
	public SelectionModel<? super E> getSelectionModel() {
		return null;
	}

	@Override
	public E getVisibleItem(int indexOnPage) {
		return null;
	}

	@Override
	public int getVisibleItemCount() {
		return 0;
	}

	@Override
	public Iterable<E> getVisibleItems() {
		return null;
	}

	@Override
	public void setSelectionModel(SelectionModel<? super E> selectionModel) {
	}
}
