package ${package}.login;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.Presenter.RevealType;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.entry.UserRolesChangeEvent.UserRolesChangeHandler;
import ${package}.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers, UserRolesChangeHandler {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public void setStatus(String status);
	}

	private final String status_loginfailed = "\u767b\u5f55\u5931\u8d25";
	
	private final CurrentUser user;
    private final PlaceManager placeManager;
	
    @ProxyCodeSplit
	@NameToken(NameTokens.login)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {
	}

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, PlaceManager placeManager, CurrentUser user) {
		//super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
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
	
	private void gotoDefaultPlace() {
		placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.home).build());
	}

	@Override
    protected void onReveal() {
		if(!user.isLogin())
			return;
		GWT.log("force to home for user had login.");
		gotoDefaultPlace();
    }
	
	@Override
	public void onUserStatusChange() {
		GWT.log("user status changed by login presenter");
		if(user.isLogin()) {
			GWT.log("user had login");
			gotoDefaultPlace();
		}
		else
			GWT.log("user is logout");
	}
}
