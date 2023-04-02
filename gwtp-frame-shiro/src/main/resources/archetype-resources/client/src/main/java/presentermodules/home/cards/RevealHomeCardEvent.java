package ${package}.presentermodules.home.cards;

import java.util.function.Consumer;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RevealHomeCardEvent extends GwtEvent<RevealHomeCardEvent.HomeCardRevealHandler> {
	public RevealHomeCardEvent(Consumer<AbstractHomeCardPresenter<?, ?>> _cardConsumer) {
		cardConsumer = _cardConsumer;
	}

	private Consumer<AbstractHomeCardPresenter<?, ?>> cardConsumer;
	
	public Consumer<AbstractHomeCardPresenter<?, ?>> getConsumer() {
		return cardConsumer;
	}

	public interface HomeCardRevealHandler extends EventHandler {
		public void onRevealHomeCard(RevealHomeCardEvent event);
	}

	private static Type<HomeCardRevealHandler> TYPE;
	
	public static Type<HomeCardRevealHandler> getType() {
		if( TYPE == null )
			TYPE = new Type<HomeCardRevealHandler>();
		
		return TYPE;
	}
	
	@Override
	public Type<HomeCardRevealHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(HomeCardRevealHandler handler) {
		handler.onRevealHomeCard(this);
	}
}
