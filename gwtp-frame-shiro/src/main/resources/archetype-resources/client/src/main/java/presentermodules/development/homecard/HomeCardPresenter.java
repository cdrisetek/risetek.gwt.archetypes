package ${package}.presentermodules.development.homecard;

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
import ${package}.presentermodules.development.TokenNames;
import ${package}.presentermodules.home.cards.IHomeCardView;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;
import ${package}.share.accounts.hosts.HostProjectRBAC;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends IHomeCardView, HasUiHandlers<MyUiHandlers> {}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {}
	private final PlaceManager placeManager;
	private final Subject subject;

	@Inject
	public HomeCardPresenter(final EventBus eventBus,
			final MyView view,
			final MyProxy proxy,
			final Subject subject,
			final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.subject = subject;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		if(!(subject.isLogin() && subject.checkRole(HostProjectRBAC.DEVELOPER)))
			return;
		getView().clear();

		getView().addAction("Icon 展示", c-> placeManager.revealPlace(iconsPlace));
		event.getConsumer().accept(this, 92 /* order */);
	}

	private final PlaceRequest iconsPlace = new PlaceRequest.Builder().nameToken(TokenNames.icons).build();
}
