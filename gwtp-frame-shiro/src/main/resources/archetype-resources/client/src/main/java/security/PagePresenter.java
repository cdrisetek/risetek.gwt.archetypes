package ${package}.security;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
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
		public void showInformation();
	}

	@ProxyStandard
	@NameToken(NameTokens.security)
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
		getView().showInformation();
	}

	@Override
	public HashMap<String, String> getSecurityInformation() {
		HashMap<String, String> infomation = new HashMap<>();
		infomation.put("名称", user.getAuthorityInfo().getUsername());
		infomation.put("密码", "******");
		return infomation;
	}

	@Override
	public HashMap<String, String> getContactInformation() {
		HashMap<String, String> infomation = new HashMap<>();
		infomation.put("电子邮件", user.getAuthorityInfo().getEmail());
		return infomation;
	}

	@Override
	public void update(String name) {
		if("名称".equals(name))
			GWT.log("update account name");
		else if("密码".equals(name)) {
			PlaceRequest placeRequest = new PlaceRequest.Builder()
					                        .nameToken(NameTokens.updatePassword)
					                        .with("back", NameTokens.security)
					                        .build();
			placeManager.revealPlace(placeRequest);
		} else if("电子邮件".equals(name)) {
			GWT.log("update email");
			PlaceRequest placeRequest = new PlaceRequest.Builder()
					                        .nameToken(NameTokens.updateEmail)
					                        .with("email", "value")
					                        .with("back", NameTokens.security)
					                        .build();
			placeManager.revealPlace(placeRequest);
		}
	}
}
