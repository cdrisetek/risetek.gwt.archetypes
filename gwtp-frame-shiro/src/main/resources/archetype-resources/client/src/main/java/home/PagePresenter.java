package ${package}.home;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import ${package}.GetResults;
import ${package}.NameTokens;
import ${package}.container.StateAction;
import ${package}.container.StateEntity;
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.home.InfoCard.InfoItem;
import ${package}.root.RootPresenter;

public class PagePresenter extends
		Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
		implements MyUiHandlers {
	
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public InfoCard appendServerStateCard();
		public InfoCard appendWelcomeCard();
	}

	@ProxyStandard
	@NameToken(NameTokens.home)
    @NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final DispatchAsync dispatcher;
	private final CurrentUser user;
	
	private InfoCard serverState;
	private InfoCard loginState;
	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view,
			final CurrentUser user,
			final MyProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
        this.dispatcher = dispatcher;
        this.user = user;
		getView().setUiHandlers(this);
		eventBus.addHandler(UserRolesChangeEvent.getType(), new UserRolesChangeEvent.UserRolesChangeHandler() {
			
			@Override
			public void onUserStatusChange() {
				updateLoginInfoCard();
			}
		});
		loginState = getView().appendWelcomeCard();
		serverState = getView().appendServerStateCard();
		updateStateInfoCard();
	}

	@Override
    protected void onReveal() {
		updateLoginInfoCard();
    }

    void updateLoginInfoCard() {
		List<InfoItem> items = new Vector<>();
		InfoItem item = new InfoItem();
		item.infoText = "\u767b\u5f55\u72b6\u6001";
		item.infoTextSecondary = user.isLogin() ? "已登录":"未登录";
		items.add(item);
		if(!user.isLogin()) {
			item = new InfoItem();
			item.infoText = "登录后拥有更多操作权限";
			items.add(item);
			loginState.updateRedirect("用户登录", "/login");
		} else {
			item = new InfoItem();
			item.infoText = "操作权限";
			Set<String> roles = user.getAuthorityInfo().getRoles();
			StringBuffer sb = new StringBuffer();
			for(String role:roles)
				sb.append(" " + role);
				
			item.infoTextSecondary = sb.toString();
			items.add(item);
			loginState.updateRedirect(null, null);
		}
			
		loginState.updateInfoItems(items);
    }
    
	void updateStateInfoCard() {
		dispatcher.execute(new StateAction(), new AsyncCallback<GetResults<StateEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server State Failed.");
			}

			@Override
			public void onSuccess(GetResults<StateEntity> result) {
				List<InfoItem> items = new Vector<>();
				for(StateEntity entity:result.getResults()) {
					InfoItem item = new InfoItem();
					item.infoText = entity.getTitle();
					item.infoTextSecondary = entity.getMessage();
					items.add(item);
				}
				
				serverState.updateInfoItems(items);
			}});
	}
}
