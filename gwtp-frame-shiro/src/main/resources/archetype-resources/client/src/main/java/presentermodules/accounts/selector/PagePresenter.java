package ${package}.presentermodules.accounts.selector;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.accounts.TokenNames;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		String getSearchKey();
		void bindSlot(Slot<PresenterWidget<?>> slot);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.accounts)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	private final Slot<PresenterWidget<?>> SLOT = new Slot<>();

	private final PlaceManager placeManager;
	private final SelectorWidget cardPresenter;
	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final PlaceManager placeManager,
			             final SelectorWidget cardPresenter) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		getView().bindSlot(SLOT);
		this.placeManager = placeManager;
		this.cardPresenter = cardPresenter;
		cardPresenter.selectedConsumer = c-> {
			String account = c.getEntity().getPrincipal();
			String selector = placeManager.getCurrentPlaceRequest().getParameter(TokenNames.selector, TokenNames.account);
			if(null != account) {
				PlaceRequest place = new PlaceRequest.Builder().nameToken(selector)
						                .with(TokenNames.select, account).build();
				placeManager.revealPlace(place, false);
			} else
				onGoBackPlace();
		};
		cardPresenter.searchKeyProvider = () -> {
			return getView().getSearchKey();
		};
		setInSlot(SLOT, cardPresenter);
	}

	@Override
	public void onGoBackPlace() {
		String selector = placeManager.getCurrentPlaceRequest().getParameter(TokenNames.selector, TokenNames.account);
		PlaceRequest place = new PlaceRequest.Builder().nameToken(selector).build();
		placeManager.revealPlace(place);
	}
	@Override
	public void onSearch() {
		cardPresenter.onRefresh();
	}
}
