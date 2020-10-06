package ${package}.presentermodules.home.cards.welcome;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickHandler;
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
import ${package}.NameTokens;
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.presentermodules.home.cards.InfoItem;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public InfoCard<?> updateInfoItems(List<InfoItem> items);
		public InfoCard<?> addAction(String title, ClickHandler handler);
		public void clear();
	}

	private final CurrentUser user;
	private final PlaceManager placeManager;

	@Inject
	public HomeCardPresenter(EventBus eventBus, CurrentUser user, MyView view, final MyProxy proxy, PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.user = user;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
		eventBus.addHandler(UserRolesChangeEvent.getType(), new UserRolesChangeEvent.UserRolesChangeHandler() {
			@Override
			public void onUserStatusChange() {
				updateLoginInfoCard();
			}
		});
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	@Override
	protected void onReveal() {
		updateLoginInfoCard();
	}

	private final PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.login).build();
	private void updateLoginInfoCard() {
		getView().clear();

		List<InfoItem> items = new ArrayList<>();
		InfoItem item = new InfoItem();
		item.infoText = "\u767b\u5f55\u72b6\u6001";
		item.infoTextSecondary = user.isLogin() ? "已登录" : "未登录";
		items.add(item);
		if (!user.isLogin()) {
			item = new InfoItem();
			item.infoText = "登录后拥有更多操作权限";
			items.add(item);
			getView().addAction("用户登录", c->{placeManager.revealPlace(placeRequest);});
		} else {
			item = new InfoItem();
			item.infoText = "操作权限";
			Set<String> roles = user.getRoles();
			StringBuffer sb = new StringBuffer();
			for (String role : roles)
				sb.append(" " + role);

			item.infoTextSecondary = sb.toString();
			items.add(item);
		}

		getView().updateInfoItems(items);
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		event.getConsumer().accept(this, 2 /* order */);
	}
}
