package ${package}.presentermodules.accounts.homecard;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.entry.Subject;
import ${package}.presentermodules.accounts.TokenNames;
import ${package}.presentermodules.home.cards.IHomeCardView;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends IHomeCardView, HasUiHandlers<MyUiHandlers> {
	}

	private final PlaceManager placeManager;
	private final Subject subject;
	@Inject
	public HomeCardPresenter(EventBus eventBus, final MyView view,
			final MyProxy proxy,
			final Subject subject,
			final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.subject = subject;
		getView().setUiHandlers(this);
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		getView().clear();
		if(!subject.isLogin())
			return;

		getView().addAction("管理项目", c-> placeManager.revealPlace(projectPlace));
		getView().addAction("管理账户", c-> placeManager.revealPlace(accountPlace));

		event.getConsumer().accept(this, 9 /* order */);
	}

	private final PlaceRequest projectPlace = new PlaceRequest.Builder().nameToken(TokenNames.project).build();
	private final PlaceRequest accountPlace = new PlaceRequest.Builder().nameToken(TokenNames.account).build();
}
