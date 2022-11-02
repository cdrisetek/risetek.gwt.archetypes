package ${package}.deployment.temporaryAccount;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
import ${package}.deployment.TokenNames;
import ${package}.entry.Subject;
import ${package}.place.root.RootPresenter;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.devops.DevOpsTaskEntity;
import ${package}.share.devops.DevOpsTaskEntity.TaskState;
import ${package}.share.devops.DevOpsTaskEntity.TaskType;
import ${package}.share.devops.DevOpsTasksAction;
import ${package}.share.dispatch.GetResults;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy>
       implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void Message(String message);
		void showTask(String title, List<String> messages, String type, String state);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.account)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final DispatchAsync dispatcher;
	private final ServerExceptionHandler exceptionHandler;
	private final PlaceManager placeManager;
	private final Subject subject;

	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final DispatchAsync dispatcher,
			             final PlaceManager placeManager,
			             final Subject subject,
			             final ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		this.subject = subject;
		getView().setUiHandlers(this);
	}

    public boolean useManualReveal() {
        return true;
    }

	@Override
    public void prepareFromRequest(PlaceRequest request) {
		if(subject.isLogin()) {
			PagePresenter.this.getProxy().manualRevealFailed();
			placeManager.revealDefaultPlace();
			return;
		}

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

				if((null != tasks) && tasks.stream().anyMatch(t -> (t.getType() == TaskType.AUTHOR) && (t.getState() != TaskState.READY))) {
					PagePresenter.this.getProxy().manualReveal(PagePresenter.this);
				} else {
					PagePresenter.this.getProxy().manualRevealFailed();
				}
			}
		});
	}
    
	@Override
	public void onTemporaryAccount() {
		AccountAction action = new AccountAction("key" /* just make handler happy */);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("create temporary account failed");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().Message("Temporary account created");
				placeManager.revealDefaultPlace();
			}
		});
	}
}
