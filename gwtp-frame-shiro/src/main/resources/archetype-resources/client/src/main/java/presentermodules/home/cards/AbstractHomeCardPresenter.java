package ${package}.presentermodules.home.cards;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;

public abstract class AbstractHomeCardPresenter<V extends View, Proxy_ extends Proxy<?>> extends Presenter<V, Proxy_>
		implements IHomeCard, RevealHomeCardEvent.HomeCardRevealHandler {
			
	public AbstractHomeCardPresenter(EventBus eventBus, V view, Proxy_ proxy) {
		super(eventBus, view, proxy);
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		event.getConsumer().accept(this);
	}

	@Override
	public int compareTo(IHomeCard o) {
		return -o.getOrder().compareTo(getOrder());
	}
}
