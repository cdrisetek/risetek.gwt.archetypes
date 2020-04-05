package ${package}.presentermodules.security;

import java.util.HashMap;
import java.util.Map;

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
		public void showInformation();
	}

	@ProxyCodeSplit
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
	}

	@Override
	public HashMap<String, Map<String, String>> getSecurityInformation() {
		HashMap<String, String> infoMap = new HashMap<>();
		infoMap.put("value", (String)user.getAuthorityInfo().getPrincipal());
		HashMap<String, Map<String, String>> infomation = new HashMap<>();
		infomation.put("名称", infoMap);

		infoMap = new HashMap<>();
		infoMap.put("value", "******");
		infoMap.put("link", NameTokens.updatePassword);
		infomation.put("密码", infoMap);
		return infomation;
	}

	@Override
	public HashMap<String, Map<String, String>> getContactInformation() {
		HashMap<String, String> infoMap = new HashMap<>();
		infoMap.put("value", user.getAttribute("email"));
		infoMap.put("link", NameTokens.updateEmail);

		HashMap<String, Map<String, String>> infomation = new HashMap<>();
		infomation.put("电子邮件", infoMap);
		return infomation;
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
}
