package ${package}.presentermodules.login;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.entry.UserRolesChangeEvent.UserRolesChangeHandler;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers, UserRolesChangeHandler {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public void setStatus(String status);
	}

	private final String status_loginfailed = "\u767b\u5f55\u5931\u8d25";
	
	private final CurrentUser user;
    private final PlaceManager placeManager;
	
    @ProxyStandard
	@NameToken(NameTokens.login)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, PlaceManager placeManager, CurrentUser user) {
		super(eventBus, view, proxy, RevealType.Root);
		getView().setUiHandlers(this);
		eventBus.addHandler(UserRolesChangeEvent.getType(), this);
		this.placeManager = placeManager;
		this.user = user;
	}

	@Override
	public void Login(String username, String password, boolean rememberme) {
		user.Login(username, password, rememberme, c->getView().setStatus(status_loginfailed));
	}
	
	@Override
    protected void onReveal() {
		if(!user.isLogin())
			return;
		GWT.log("force to home for user had login.");
		placeManager.revealDefaultPlace();
    }
	
	@Override
	public void onUserStatusChange() {
		GWT.log("user status changed by login presenter");
		if(user.isLogin()) {
			GWT.log("user had login");
			placeManager.revealDefaultPlace();
		}
		else
			GWT.log("user is logout");
	}

	@Override
	public void newAccount() {
		PlaceRequest placeRequest = new PlaceRequest.Builder()
				                        .with("continue", NameTokens.login)
				                        .nameToken(NameTokens.newAcct).build();
		placeManager.revealPlace(placeRequest);
	}

	@Override
	public void resetPassword() {
		PlaceRequest placeRequest = new PlaceRequest.Builder()
                .with("continue", NameTokens.login)
                .nameToken(NameTokens.resetPassword).build();
		placeManager.revealPlace(placeRequest);
	}
}
