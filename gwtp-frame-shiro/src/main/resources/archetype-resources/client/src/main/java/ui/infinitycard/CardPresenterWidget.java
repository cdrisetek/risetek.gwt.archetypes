package ${package}.ui.infinitycard;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import ${package}.utils.ServerExceptionHandler;

public abstract class CardPresenterWidget<E extends Comparable<E>, C extends CardWidget<E>,
                                          H extends CardUiHandlers<E>,
                                          V extends CardInfinityView<E, C, H>>
                extends PresenterWidget<V> implements CardUiHandlers<E> {

	public interface CardView<VE, VH extends CardUiHandlers<VE>> extends View, HasUiHandlers<VH> {
		void updateRowData(int start, List<? extends VE> list, final LoadRange loadRange);
		void refresh();
	}

	protected final ServerExceptionHandler exceptionHandler;
	protected final DispatchAsync dispatcher;

	@SuppressWarnings("unchecked")
	public CardPresenterWidget(final EventBus eventBus,
                               final DispatchAsync dispatcher,
                               final ServerExceptionHandler exceptionHandler,
			                   final V view) {
		super(eventBus, view);
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		getView().setUiHandlers((H)this);
	}

	@Override
	public void onRefresh() {
		getView().refresh();
	}

	public Supplier<String> searchKeyProvider;
	protected String getSearchKey() {
		return null == searchKeyProvider ? null : searchKeyProvider.get();
	}

	public Consumer<CardWidget<E>> selectedConsumer;
	@Override
	public void onSelected(CardWidget<E> card) {
		if(null == selectedConsumer)
			return;
		selectedConsumer.accept(card);
	}
	
	@Override
	public void onReveal() {
		onRefresh();
	}
}
