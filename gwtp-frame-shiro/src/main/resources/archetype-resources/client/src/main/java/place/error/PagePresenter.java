package ${package}.place.error;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.place.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers, RevealErrorEventHandler {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void setErrorMessage(String message);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.error)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	private final PlaceManager placeManager;
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final PlaceManager placeManager,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}
	
	@Override
	public void onReveal() {
		PlaceRequest request = placeManager.getCurrentPlaceRequest();
		String message = request.getParameter("message", null);
		if(null != message) {
			GWT.log("error message:" + message);
			getView().setErrorMessage(message);
		}
	}

	@ProxyEvent
	@Override
	public void onRevealErrorPlace(RevealErrorEvent event) {
		getView().setErrorMessage(event.exceptionName);
		forceReveal();
	}
}
