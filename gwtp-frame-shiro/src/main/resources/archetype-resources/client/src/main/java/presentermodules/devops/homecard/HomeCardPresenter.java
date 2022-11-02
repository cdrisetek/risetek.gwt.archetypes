package ${package}.presentermodules.devops.homecard;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.Subject;
import ${package}.presentermodules.devops.TokenNames;
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
		if(!(subject.isLogin() && (subject.checkRole(HostProjectRBAC.DEVELOPER)) || (subject.checkRole(HostProjectRBAC.MAINTANCE))))
			return;
		getView().clear();

		getView().addAction("OAuth客户端模拟", c-> placeManager.revealPlace(clientPlace));
		getView().addAction("部署信息", c-> placeManager.revealPlace(deployPlace));
		getView().addAction("数据维护", c-> placeManager.revealPlace(dataPlace));
		event.getConsumer().accept(this, 90 /* order */);
	}

	private final PlaceRequest deployPlace = new PlaceRequest.Builder().nameToken(NameTokens.deploy).build();
	private final PlaceRequest dataPlace = new PlaceRequest.Builder().nameToken(TokenNames.datamaintenance).build();
	private final PlaceRequest clientPlace = new PlaceRequest.Builder().nameToken(TokenNames.client).build();
}
