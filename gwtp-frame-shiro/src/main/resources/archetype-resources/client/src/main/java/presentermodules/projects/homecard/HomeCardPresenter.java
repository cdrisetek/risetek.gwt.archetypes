package ${package}.presentermodules.projects.homecard;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;
import ${package}.presentermodules.projects.TokenNames;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	private final PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(TokenNames.projects).build();

	private final PlaceManager placeManager;
	@Inject
	public HomeCardPresenter(EventBus eventBus, MyView view, final MyProxy proxy, PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		event.getConsumer().accept(this, 9 /* order */);
	}

	@Override
	public void reveal() {
		placeManager.revealPlace(placeRequest);
	}
}
