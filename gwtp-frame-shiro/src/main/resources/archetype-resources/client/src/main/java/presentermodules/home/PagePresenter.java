package ${package}.presentermodules.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.NameTokens;
import ${package}.root.RootPresenter;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	public static final Slot<PresenterWidget<?>> SLOT_CARD0 = new Slot<>();
	public static final Slot<PresenterWidget<?>> SLOT_CARD1 = new Slot<>();
	public static final Slot<PresenterWidget<?>> SLOT_CARD2 = new Slot<>();

	@ProxyStandard
	@NameToken(NameTokens.home)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);

		fireEvent(new RevealHomeCardEvent((p, o) -> {
			switch(o % 3) {
			case 0:
				addToSlot(SLOT_CARD0, p);
				break;
			case 1:
				addToSlot(SLOT_CARD1, p);
				break;
			case 2:
				addToSlot(SLOT_CARD2, p);
				break;
			}
		}));
	}
}
