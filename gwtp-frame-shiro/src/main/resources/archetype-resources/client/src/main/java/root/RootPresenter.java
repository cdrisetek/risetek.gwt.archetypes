package ${package}.root;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.root.RevealDefaultLinkColumnHandler.RevealDefaultLinkColumnEvent;

/**
 * 'RevealType.Root' make this Container on the bottom.
 * @author wangyc@risetek.com
 *
 */
public class RootPresenter extends Presenter<RootPresenter.MyView, RootPresenter.MyProxy> 
	implements MyUiHandlers {
    public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
    }

    public static final NestedSlot SLOT_MainContent = new NestedSlot();
    public static final NestedSlot SLOT_MenuContent = new NestedSlot();
    public static final NestedSlot SLOT_LeftContent = new NestedSlot();

    private DispatchAsync dispatcher;
    private final PlaceManager placeManager;
    
    @ProxyStandard
    @NoGatekeeper
    public interface MyProxy extends Proxy<RootPresenter> {}

    @Inject
    public RootPresenter(EventBus eventBus, MyView view, MyProxy proxy, 
    		DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, RevealType.Root);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

	@Override
	public void onReveal() {
		// To reveal menu module.
        fireEvent(new RevealDefaultLinkColumnEvent());
	}
}
