package ${package}.presentermodules.home.cards.state;

import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import ${package}.GetResults;
import ${package}.container.StateAction;
import ${package}.container.StateEntity;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.presentermodules.home.cards.InfoItem;
import ${package}.presentermodules.home.cards.RevealHomeCardEvent;

public class HomeCardPresenter extends Presenter<HomeCardPresenter.MyView, HomeCardPresenter.MyProxy>
		implements MyUiHandlers, RevealHomeCardEvent.HomeCardRevealHandler {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public InfoCard<?> updateInfoItems(List<InfoItem> items);

		public InfoCard<?> updateRedirect(String title, String link);
	}

	@ProxyStandard
	@NoGatekeeper
	public interface MyProxy extends Proxy<HomeCardPresenter> {
	}

	private final DispatchAsync dispatcher;

	@Inject
	public HomeCardPresenter(EventBus eventBus, DispatchAsync dispatcher, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		updateStateInfoCard();
	}

	private void updateStateInfoCard() {
		dispatcher.execute(new StateAction(), new AsyncCallback<GetResults<StateEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server State Failed.");
			}

			@Override
			public void onSuccess(GetResults<StateEntity> result) {
				List<InfoItem> items = new Vector<>();
				for (StateEntity entity : result.getResults()) {
					InfoItem item = new InfoItem();
					item.infoText = entity.getTitle();
					item.infoTextSecondary = entity.getMessage();
					items.add(item);
				}

				getView().updateInfoItems(items);
			}
		});
	}

	@ProxyEvent
	@Override
	public void onRevealHomeCard(RevealHomeCardEvent event) {
		GWT.log("add state homecard");
		event.getConsumer().accept(this, 1);
		updateStateInfoCard();
	}
}
