package ${package}.presentermodules.home.cards.simple;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.bindery.IBuilderStamp;
import ${package}.presentermodules.home.cards.IHomeCardView;
import ${package}.presentermodules.home.cards.InfoItem;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends IHomeCardView, HasUiHandlers<MyUiHandlers> {
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	@Inject
	public HomeCardPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
	}

	private final static IBuilderStamp stamp = GWT.create(IBuilderStamp.class);
	
	private void updateLoginInfoCard() {
		getView().clear();

		List<InfoItem> items = new ArrayList<>();
		InfoItem item = new InfoItem();
		item.infoText = "Git commit ID";
		item.infoTextSecondary = stamp.getCommitID();
		items.add(item);

		item = new InfoItem();
		item.infoText = "Git Commit Date Time";
		item.infoTextSecondary = stamp.getCommitDate();
		items.add(item);

		item = new InfoItem();
		item.infoText = "Build Date Time";
		item.infoTextSecondary = stamp.getBuilderStamp();
		items.add(item);

		getView().updateInfoItems(items);
	}
	
	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		updateLoginInfoCard();
		event.getConsumer().accept(this, 0 /* order */);
	}
}
