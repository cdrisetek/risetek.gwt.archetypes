package ${package}.presentermodules.security.password;

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
import ${package}.entry.Subject;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.updatePassword)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	private final Subject subject;
	private final PlaceManager placeManager;
	
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final PlaceManager placeManager,
			final Subject subject,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.subject = subject;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	/**
	 * reset password with password == "";
	 */
	@Override
	public void changePassword(String newPassword) {
		subject.changePassword(newPassword, c->{
			if(c.equals("success"))
				goContinue();
		});
	}

	@Override
	public void goContinue() {
		String backto = placeManager.getCurrentPlaceRequest().getParameter("continue", NameTokens.home);
		PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(backto).build();
		placeManager.revealPlace(placeRequest);
	}
}
