package ${package}.presentermodules.security.email;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.CurrentUser;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.updateEmail)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	private final CurrentUser user;
	private final PlaceManager placeManager;

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final CurrentUser user,
			final PlaceManager placeManager,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.user = user;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	@Override
	public void changeEmail(String newPassword) {
		user.changeEmail(newPassword,
				c->{
					GWT.log("change email:" +c);
					goContinue();
					}
				);
	}

	@Override
	public String getOriginEmail() {
		return user.getAttribute("email");
	}

	@Override
	public void goContinue() {
		String backto = placeManager.getCurrentPlaceRequest().getParameter("continue", NameTokens.home);
		PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(backto).build();
		placeManager.revealPlace(placeRequest);
	}
}
