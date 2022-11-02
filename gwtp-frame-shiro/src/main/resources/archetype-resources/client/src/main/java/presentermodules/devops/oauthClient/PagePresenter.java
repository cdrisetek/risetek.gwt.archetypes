package ${package}.presentermodules.devops.oauthClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.devops.TokenNames;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void Message(String message);
		void tokenPanelSetVisible(boolean visible);
		void setCode(String value);
		void setState(String value);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.client)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final PlaceManager placeManager;

	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final PlaceManager placeManager) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		getView().setUiHandlers(this);

	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}

	@Override
	public void onReveal() {
		getView().tokenPanelSetVisible(false);
		String code = placeManager.getCurrentPlaceRequest().getParameter("code", null);
		String state = placeManager.getCurrentPlaceRequest().getParameter("state", null);
		if(null != code || null != state) {
			getView().setCode(code);
			getView().setState(state);
			getView().tokenPanelSetVisible(true);
		}
	}
	/**
	 * Simulate a OAuth client, send request to OAuth service,
	 * get and analysis response from service.
	 * username, password
	 * client_id
	 * redirect_uri
	 * response_type
	 */
	@Override
	public void onOAuthClientRequest() {
		// TODO: how to clear Location Hash?
		clearHash();

		UrlBuilder builder = new UrlBuilder();
		builder.setProtocol(Window.Location.getProtocol())
		       .setHost(Window.Location.getHost())
		       .setPath("/demo/oauth/login");
		
		String port = Window.Location.getPort();
		if (port != null && port.length() > 0) {
			builder.setPort(Integer.parseInt(port));
		}
		Location.replace(builder.buildString());
//		Location.assign(Location.createUrlBuilder().setPath("/demo/oauth/login").buildString());
	}

    public static native void clearHash() /*-{
         window.location.hash = '';
         console.log("clear hash");
         history.replaceState(null, null, ' ');
         history.replaceState({}, document.title, ".");
         
		var uri = window.location.toString();
         console.log("uri hash:" + uri);
  
            if (uri.indexOf("#") > 0) {
                var clean_uri = uri.substring(0, 
                                uri.indexOf("#"));
  
         console.log("clear hash:" + clean_uri);
                window.history.replaceState({}, 
                        document.title, clean_uri);
            }         
         
  }-*/;

	@Override
	public void onOAuthTokenRequest(String code, String state) {
		String requestData = "response_type=access_token" + "&code=" + code + "&state="+state;
		XMLHttpRequest request = XMLHttpRequest.create();
		request.open("GET", "/demo/oauth/token?" + requestData);
		request.setOnReadyStateChange( xhr -> {
			if(xhr.getReadyState() == XMLHttpRequest.DONE) {
				GWT.log("it is done");
				GWT.log("return: " + xhr.getResponseText());
			}
		});
		request.send();
	}

}
