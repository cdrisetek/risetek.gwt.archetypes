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

public abstract class CardPresenterWidget<E, C extends CardWidget<E>>
		        extends PresenterWidget<CardInfinityView<E, C>>
                implements CardUiHandlers<E> {

	public interface CardView<E, C> extends View, HasUiHandlers<CardUiHandlers<E>> {
		void updateRowData(int start, List<? extends E> list, final LoadRange loadRange);
		void refresh();
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
	}

	@Override
	public void onRefresh() {
		getView().refresh();
	}

	public Supplier<String> searchKeyProvider;
	protected String getSearchKey() {
		return null == searchKeyProvider ? null : searchKeyProvider.get();
	}

	public Consumer<IsCardWidget<E>> selectedConsumer;
	@Override
	public void onSelected(IsCardWidget<E> card) {
		if(null == selectedConsumer)
			return;
		selectedConsumer.accept(card);
	}
	
	@Override
	public void onReveal() {
		onRefresh();
	}
}
