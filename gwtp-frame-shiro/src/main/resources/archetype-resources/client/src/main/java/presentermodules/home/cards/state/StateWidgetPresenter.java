package ${package}.presentermodules.home.cards.state;

import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import ${package}.GetResults;
import ${package}.container.StateAction;
import ${package}.container.StateEntity;
import ${package}.presentermodules.home.cards.InfoCard;
import ${package}.presentermodules.home.cards.InfoItem;

public class StateWidgetPresenter extends PresenterWidget<StateWidgetPresenter.MyView> implements MyUiHandlers {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public InfoCard<?> updateInfoItems(List<InfoItem> items);
		public InfoCard<?> updateRedirect(String title, String link);
	}
	
	private final DispatchAsync dispatcher;
	
	@Inject
	public StateWidgetPresenter(EventBus eventBus, DispatchAsync dispatcher, MyView view) {
		super(eventBus, view);
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
				for(StateEntity entity:result.getResults()) {
					InfoItem item = new InfoItem();
					item.infoText = entity.getTitle();
					item.infoTextSecondary = entity.getMessage();
					items.add(item);
				}
				
				getView().updateInfoItems(items);
			}});
	}
}
