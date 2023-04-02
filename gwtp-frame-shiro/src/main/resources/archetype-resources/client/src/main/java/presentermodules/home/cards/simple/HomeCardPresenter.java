package ${package}.presentermodules.home.cards.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.bindery.IBuilderStamp;
import ${package}.presentermodules.home.cards.AbstractHomeCardPresenter;
import ${package}.presentermodules.home.cards.IHomeCardView;
import ${package}.presentermodules.home.cards.InfoItem;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends AbstractHomeCardPresenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
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
		List<InfoItem> items = new ArrayList<>();
		InfoItem item;
		item = new InfoItem();
		item.infoText = "Build Date Time";
		item.infoTextSecondary = Arrays.asList(stamp.getBuilderStamp());
		items.add(item);

		if(null != stamp.getCommitID()) {
			item = new InfoItem();
			item.infoText = "Git commit ID";
			item.infoTextSecondary = Arrays.asList(stamp.getCommitID());
			items.add(item);
		}

		if(null != stamp.getCommitDate()) {
			item = new InfoItem();
			item.infoText = "Git Commit Date Time";
			item.infoTextSecondary = Arrays.asList(stamp.getCommitDate());
			items.add(item);
		}

		getView().updateInfoItems(items);
	}
	
	@Override
	public boolean update() {
		getView().clear();
		updateLoginInfoCard();
		return true;
	}

	@Override
	public Integer getOrder() {
		return 9000;
	}
}
