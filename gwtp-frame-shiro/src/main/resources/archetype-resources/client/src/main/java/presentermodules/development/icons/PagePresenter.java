package ${package}.presentermodules.development.icons;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.bindery.PlainMenu;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.accounts.TokenNames;
import ${package}.utils.Icons;
import ${package}.utils.Icons.ArrowLeft;
import ${package}.utils.Icons.ArrowRight;
import ${package}.utils.Icons.Convert;
import ${package}.utils.Icons.Home;
import ${package}.utils.Icons.Login;
import ${package}.utils.Icons.Modify;

@PlainMenu(order = 1000, title = "\u5f00\u53d1\u8005\u4e50\u56ed", token = TokenNames.icons, iconClass = Icons.Convert.class)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void append(String name, Element icon);
		void append(String name, Widget icon);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.icons)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final PlaceManager placeManager;

	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final PlaceManager placeManager) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		getView().setUiHandlers(this);

		getView().append("ArrowRight", new ArrowRight());
		getView().append("ArrowLeft", new ArrowLeft());
		getView().append("Login", new Login());
		getView().append("Modify", new Modify());
		getView().append("Convert", new Convert());
		getView().append("Home", new Home());
		getView().append("matIcon", Icons.matIcon());
		getView().append("helpIcon", Icons.helpIcon());

		getView().append("serverIcon", Icons.serverIcon());
		getView().append("aboutIcon", Icons.aboutIcon());
		getView().append("eyeSlashIcon", Icons.eyeSlashIcon());
		getView().append("eyeIcon", Icons.eyeIcon());
		getView().append("compassIcon", Icons.compassIcon());
		getView().append("helpIcon", Icons.helpIcon());
		getView().append("helpIcon", Icons.helpIcon());
		getView().append("helpIcon", Icons.helpIcon());
}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}
}
