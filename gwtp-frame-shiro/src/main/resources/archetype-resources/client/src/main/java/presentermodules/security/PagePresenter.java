package ${package}.presentermodules.security;

import java.util.List;
import java.util.Vector;

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
import ${package}.share.users.EnumUserDescription;

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
	public List<informationItem> getSecurityInformation() {
		List<informationItem> items = new Vector<>();
		informationItem item = new informationItem();
		item.key = "名称";
		String principal = user.getAccountAttribute(EnumUserDescription.PRINCIPAL.name());
		item.value = (null == principal)?"UNKNOW":principal;
		items.add(item);
		
		item = new informationItem();
		item.key = "密码";
		item.value = "******";
		item.link = NameTokens.updatePassword;
		
		items.add(item);
		
		return items;
	}

	@Override
	public List<informationItem> getContactInformation() {
		List<informationItem> items = new Vector<>();
		informationItem item = new informationItem();
		item.key = "电子邮件";
		item.value = user.getAccountAttribute(EnumUserDescription.EMAIL.name());
		item.link = NameTokens.updateEmail;
		items.add(item);
		return items;
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
