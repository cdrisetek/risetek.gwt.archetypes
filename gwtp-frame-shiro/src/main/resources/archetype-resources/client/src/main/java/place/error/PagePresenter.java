package ${package}.place.error;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.NameTokens;
import ${package}.place.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.error)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
	}
}
