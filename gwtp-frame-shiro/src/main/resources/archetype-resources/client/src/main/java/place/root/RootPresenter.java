package ${package}.place.root;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.place.root.RevealMenuEventHandler.RevealMenuEvent;

/**
 * 'RevealType.Root' make this Container on the bottom.
 * @author wangyc@risetek.com
 *
 */
public class RootPresenter extends Presenter<RootPresenter.MyView, RootPresenter.MyProxy> 
	implements MyUiHandlers {
    public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
    	void bindMainSlot(NestedSlot slot);
    	void bindMenuSlot(NestedSlot slot);
    	void bindLeftSlot(NestedSlot slot);
    }

    public static final NestedSlot SLOT_MainContent = new NestedSlot();
    public static final NestedSlot SLOT_MenuContent = new NestedSlot();
    public static final NestedSlot SLOT_LeftContent = new NestedSlot();

	@ProxyCodeSplit
    @NoGatekeeper
    public interface MyProxy extends Proxy<RootPresenter> {}

    @Inject
    public RootPresenter(EventBus eventBus, MyView view, MyProxy proxy, 
    		DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, RevealType.Root);
        getView().setUiHandlers(this);
        getView().bindMainSlot(SLOT_MainContent);
        getView().bindMenuSlot(SLOT_MenuContent);
        getView().bindLeftSlot(SLOT_LeftContent);
    }

	@Override
	public void onReveal() {
		// To reveal menu module.
        fireEvent(new RevealMenuEvent());
	}
}
