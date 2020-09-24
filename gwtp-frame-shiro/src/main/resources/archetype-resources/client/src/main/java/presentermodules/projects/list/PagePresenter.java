package ${package}.presentermodules.projects.list;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.bindery.PlainMenu;
import ${package}.entry.CurrentUser;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.presentermodules.projects.TokenNames;
import ${package}.root.RootPresenter;
import ${package}.ui.infinitycard.HasSearch;

@PlainMenu(order = 1010, title = "项目管理", token = TokenNames.projects)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers, HasSearch {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void alert(String message);
		String getSearchKey();
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.projects)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {
	}

	private final CurrentUser user;
	public static final Slot<PresenterWidget<?>> CARD_DISPLAY_SLOT = new Slot<>();

	private final ProjectCardPresenterWidget cardPresenter;
	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final CurrentUser user,
			             final ProjectCardPresenterWidget cardPresenter) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.user = user;
		this.cardPresenter = cardPresenter;
		this.cardPresenter.setSearchKeyHandler(this);
		setInSlot(CARD_DISPLAY_SLOT, cardPresenter);
		getView().setUiHandlers(this);
	}

	@Override
	public void onCreateProjectPlace() {
		if(!user.checkRole("admin")) {
			getView().alert("you have no privage to create new account");
			return;
		}
		cardPresenter.openCardDialog(null);
	}
	
	@Override
	public void onSearch() {
		cardPresenter.onRefresh();
	}

	@Override
	public String getSearchKey() {
		return getView().getSearchKey();
	}
}
