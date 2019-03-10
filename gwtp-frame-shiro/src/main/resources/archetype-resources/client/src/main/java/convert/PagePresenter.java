package ${package}.convert;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.NameTokens;
import ${package}.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public void showTranslation(String text);
	}

	@ProxyStandard
	@NameToken(NameTokens.convert)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
	}
	
	@Override
	public void InputChanged(String text) {
		
		StringBuilder builder = new StringBuilder();
		for(char ch: text.toCharArray()) {
		    if(ch >= 0x20 && ch <= 0x7E) {
		        builder.append(ch);
		    } else {
		        // builder.append(String.format("\\u%04X", (int)ch));
		    	builder.append("\\u").append(Integer.toHexString((int)ch));
		    }
		}

		String result = builder.toString();
		getView().showTranslation(result);
		GWT.log("text:" + result);
	}
}
