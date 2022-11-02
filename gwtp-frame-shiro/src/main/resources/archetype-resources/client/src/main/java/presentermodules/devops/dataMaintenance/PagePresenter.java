package ${package}.presentermodules.devops.dataMaintenance;

import java.util.HashSet;
import java.util.Set;

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
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.devops.TokenNames;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.share.accounts.projects.EnumProject;
import ${package}.share.accounts.projects.ProjectAction;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.devops.DevOpsAction;
import ${package}.share.devops.DevOpsAction.OP;
import ${package}.share.dispatch.GetNoResult;
import ${package}.share.dispatch.GetResults;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void Message(String message);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.datamaintenance)
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

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}
	@Override
	public void onClearProject() {
		DevOpsAction action = new DevOpsAction(OP.CLEAR_PROJECT);
		dispatcher.execute(action, new AsyncCallback<GetNoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetNoResult result) {
				getView().Message("Project Datas cleared");
			}
		});
	}
	
	private ProjectEntity randomProjectEntity() {
		int index = (int)(Math.random() * 10000);
		String projectName = "Project" + index;
		ProjectEntity projectEntity = new ProjectEntity();
		projectEntity.setName(projectName);
		
		if(index % 2 == 0) {
			projectEntity.setDescription(EnumProject.NOTES, "This is a long long long long long long long long"
					+ " long long long long long long long long long long long"
					+ " long long long long long long long long long long long"
					+ " long long note");
		} else {
			projectEntity.setDescription(EnumProject.NOTES, "This is a short note");
		}
		return projectEntity;
	}

	private long sequence;
	@Override
	public void onRandomProject() {
		Set<ProjectEntity> entities = new HashSet<>();
		for(int i=0; i < 100; i++)
			entities.add(randomProjectEntity());

		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.UPSERT, null, 0, 0 /* don't care */);
		sequence = Math.max(sequence, action.sequence);

		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				getView().Message("Random Project Datas Generated");
			}
		});
	}
	@Override
	public void onClearAccount() {
		// TODO Auto-generated method stub
		
	}
	
	private AccountEntity randomAccountEntity() {
		AccountEntity accountEntity = new AccountEntity();
		int index = (int)(Math.random() * 10000);
		String username = "admin" + index + "@risetek.com";
		accountEntity.setPrincipal(username);
		accountEntity.setDescription(EnumAccount.EMAIL, username);

		if (index % 2 == 0)
			accountEntity.setDescription(EnumAccount.NOTES,
					"this is a very long long long long long long long "
							+ "long long long long long long long long long long long long long "
							+ "long long long long long long long long long long long long long "
							+ "long long long long long long long long long long long long long "
							+ "long long long long long long long long long long long long notes");
		else
			accountEntity.setDescription(EnumAccount.NOTES, "this is short notes.");
		return accountEntity;
	}
	
	@Override
	public void onRandomAccount() {
		Set<AccountEntity> entities = new HashSet<>();
		for(int i=0; i < 100; i++)
			entities.add(randomAccountEntity());
		
		AccountAction action = new AccountAction("password", entities);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().Message("测试账户数据成功创建");
			}
		});
	}
}
