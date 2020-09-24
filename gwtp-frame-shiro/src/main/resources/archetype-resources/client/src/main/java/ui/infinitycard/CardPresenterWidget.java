package ${package}.ui.infinitycard;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import ${package}.utils.ServerExceptionHandler;

public abstract class CardPresenterWidget<E, C extends CardWidget<E>>
		extends PresenterWidget<CardInfinityView<E, C>> implements CardUiHandlers<E>, DialogUiHandlers<E> {
	public interface CardView<E, C> extends View, HasUiHandlers<CardUiHandlers<E>>, HasData<E> {
		// Ui operation for remove card panel from parent Panel.
		void onCardRemove(IsWidget widget);
		void onCardUpdate(IsCardWidget<E> card);
		public void setDialogUiHandlers(DialogUiHandlers<E> uiHandlers);

		public void setDialogFocus(IsCardWidget<E> card);
		public E getDialogEntity(IsCardWidget<E> card);
		public void showDialog(IsCardWidget<E> card);
		public boolean checkCommitValid(IsCardWidget<E> card);
	}

	protected final ServerExceptionHandler exceptionHandler;
	protected final DispatchAsync dispatcher;

	public CardPresenterWidget(final EventBus eventBus,
                               final DispatchAsync dispatcher,
                               final ServerExceptionHandler exceptionHandler,
			                   final CardInfinityView<E, C> view) {
		super(eventBus, view);
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		getView().setUiHandlers(this);
		getView().setDialogUiHandlers(this);
		// XXX: for now, this class construction not yet complete, so scheduler to deferred add data display.
		Scheduler.get().scheduleDeferred(()->getDataProvider().addDataDisplay(getView()));
	}

	protected abstract AsyncDataProvider<E> getDataProvider();

	@Override
	public void onRefresh() {
		getView().setVisibleRangeAndClearData(null, true);
	}

	private HasSearch keyhandler;
	@Override
	public void setSearchKeyHandler(HasSearch keyhandler) {
		this.keyhandler = keyhandler;
	}

	protected String getSearchKey() {
		if(null != keyhandler)
			return keyhandler.getSearchKey();
		return null;
	}

	@Override
	public void openCardDialog(final IsCardWidget<E> widget) {
		getView().showDialog(widget);
	}

	@Override
	public boolean checkCommitValid(IsCardWidget<E> card) {
		return getView().checkCommitValid(card);
	}
	
	@Override
	public void onEditorCommit(IsCardWidget<E> card) {
		// Update entity with Dialog Contents.
		E e = getView().getDialogEntity(card);
		if(null == card) {
			Set<E> entities = new HashSet<>();
			entities.add(e);
			onCreateEntities(entities);
		} else {
			Set<IsCardWidget<E>> cards = new HashSet<>();
			cards.add(card);
			onUpdateCards(cards);
		}
	}
}
