package ${package}.presentermodules.home.cards.simple;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.presentermodules.home.cards.IHomeCardView;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends IHomeCardView, HasUiHandlers<MyUiHandlers> {
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	@Inject
	public HomeCardPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		event.getConsumer().accept(this, 0 /* order */);
	}
}
