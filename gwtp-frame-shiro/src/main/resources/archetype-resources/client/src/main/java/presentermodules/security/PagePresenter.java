package ${package}.presentermodules.security;

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
import ${package}.entry.LoggedInGatekeeper;
import ${package}.entry.Subject;
import ${package}.place.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void showAccountView();
		void showPasswordView();
		void showEmailView();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.security)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}
	
	private final Subject subject;
	private final PlaceManager placeManager;
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final Subject subject,
			final PlaceManager placeManager,
			final MyProxy proxy) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.subject = subject;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	@Override
	public void onReveal() {
		getView().showAccountView();
	}

	@Override
	public void update(String name) {
		if(null == name)
			return;

		PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(name)
                .with("continue", NameTokens.security)
                .build();
		placeManager.revealPlace(placeRequest);
	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}

	@Override
	public String getSecurityInformation(String key) {
		if(null == key)
			return subject.getSubjectPrincipal();
		return subject.getAccountAttribute(key);
	}

	@Override
	public void showPasswordView() {
		getView().showPasswordView();
	}

	@Override
	public void showEmailView() {
		getView().showEmailView();
	}

	@Override
	public void updateEmail(String value) {
		subject.changeEmail(value, c->{
					GWT.log("change email:" +c);
					getView().showAccountView();
					}
				);
	}

	@Override
	public void updatePassword(String value) {
		subject.changePassword(value, c->{
			if(c.equals("success"))
				getView().showAccountView();
		});
	}
}
