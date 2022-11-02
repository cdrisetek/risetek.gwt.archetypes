package ${package}.deployment;

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
import ${package}.NameTokens;
import ${package}.entry.Subject;
import ${package}.place.root.RootPresenter;
import ${package}.share.accounts.hosts.HostProjectRBAC;
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
		void showAccount();
		void showDeploy();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.deploy)
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
			             final Subject subject,
			             final PlaceManager placeManager,
			             final ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		this.subject = subject;
		getView().setUiHandlers(this);
	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}
	
	private final PlaceRequest tasksPlace = new PlaceRequest.Builder().nameToken(TokenNames.tasks).build();
	@Override
	public void onTasksPlace() {
		placeManager.revealPlace(tasksPlace);
	}

	// XXX: account place
	private final PlaceRequest accountPlace = new PlaceRequest.Builder().nameToken("/account").build();
	@Override
	public void onAccountPlace() {
		placeManager.revealPlace(accountPlace);
	}

	public boolean useManualReveal() {
        return true;
    }
	
	private final PlaceRequest temporaryPlace = new PlaceRequest.Builder().nameToken(TokenNames.account).build();

	@Override
    public void prepareFromRequest(PlaceRequest request) {
		if(subject.isLogin()) {
			if (!subject.checkRole(HostProjectRBAC.DEVELOPER)) {
				getView().showAccount();
				getView().showDeploy();
				PagePresenter.this.getProxy().manualReveal(PagePresenter.this);
				return;
			} else
				GWT.log("It looks like you're developer");
		}

		DevOpsTasksAction action = new DevOpsTasksAction();
		dispatcher.execute(action, new AsyncCallback<GetResults<DevOpsTaskEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				PagePresenter.this.getProxy().manualRevealFailed();
				exceptionHandler.handler(caught);
				placeManager.revealDefaultPlace();
			}

			@Override
			public void onSuccess(GetResults<DevOpsTaskEntity> result) {
				List<DevOpsTaskEntity> tasks = result.getResults();

				if((null != tasks) && tasks.stream().anyMatch(t -> (t.getType() == TaskType.AUTHOR) && (t.getState() != TaskState.READY))) {
					// without basic authorization, redirect to temporary Account page.
					PagePresenter.this.getProxy().manualRevealFailed();
					placeManager.revealPlace(temporaryPlace, false);
				} else {
					getView().showDeploy();
					PagePresenter.this.getProxy().manualReveal(PagePresenter.this);
				}
			}
		});
	}
}
