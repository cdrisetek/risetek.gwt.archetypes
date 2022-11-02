package ${package}.deployment.tasks;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.deployment.TokenNames;
import ${package}.place.root.RootPresenter;
import ${package}.share.devops.DevOpsTaskEntity;
import ${package}.share.devops.DevOpsTasksAction;
import ${package}.share.dispatch.GetResults;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
       implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void Message(String message);
		void showTask(String title, List<String> messages, String type, String state);
		void clear();
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.tasks)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final DispatchAsync dispatcher;
	private final ServerExceptionHandler exceptionHandler;
	private final PlaceManager placeManager;

	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final DispatchAsync dispatcher,
			             final PlaceManager placeManager,
			             final ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		getView().setUiHandlers(this);
	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.deploy).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}

    public boolean useManualReveal() {
        return true;
    }

	@Override
    public void prepareFromRequest(PlaceRequest request) {
		DevOpsTasksAction action = new DevOpsTasksAction();
		dispatcher.execute(action, new AsyncCallback<GetResults<DevOpsTaskEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				PagePresenter.this.getProxy().manualRevealFailed();
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<DevOpsTaskEntity> result) {
				List<DevOpsTaskEntity> tasks = result.getResults();
				getView().clear();
				for(DevOpsTaskEntity entity:tasks) {
					getView().showTask(entity.getTitle(), entity.getMessage(), entity.getType().name(), entity.getState().name());
				}
				
				PagePresenter.this.getProxy().manualReveal(PagePresenter.this);
			}
		});
	}
}
