package ${package}.presentermodules.accounts.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.place.root.RootPresenter;
import ${package}.presentermodules.accounts.TokenNames;
import ${package}.share.accounts.projects.ProjectAction;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.accounts.roles.RoleAction;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetResult;
import ${package}.share.dispatch.GetResults;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction.OnException;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void showProjectView();
		void showCreateView();
		void showEditView();
		void showRoleView();
		void Message(String message);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.project)
	@UseGatekeeper(LoggedInGatekeeper.class)
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
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}
	
	private void projectsRead(BiConsumer<Throwable, GetResults<ProjectEntity>> consumer, Set<ProjectEntity> entities) {
		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.READ, null, 0, 1 /* one only */);
		sequence = Math.max(sequence, action.sequence);

		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				consumer.accept(caught, null);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				consumer.accept(null, result);
			}
		});
	}
	
	private String selectedAccount, selectedProject;
	
	@Override
	public void onReveal() {
		selectedAccount = placeManager.getCurrentPlaceRequest().getParameter(TokenNames.select, selectedAccount);
		selectedProject = placeManager.getCurrentPlaceRequest().getParameter(TokenNames.selectProject, selectedProject);
		getView().showProjectView();
		super.onReveal();
	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}

	private long sequence;
	@Override
	public void checkValidate(String value, Consumer<ProjectValidate> checker) {
		if(null == value || value.isEmpty())
			checker.accept(ProjectValidate.EMPTY);
		else if(value.length() > 4) {
			checker.accept(ProjectValidate.CHECKING);

			ProjectEntity entity = new ProjectEntity();
			entity.setName(value);
			projectsRead((caught , result) -> {
				if(null != caught)
					exceptionHandler.handler(caught);
				else if(null != result) {
					if(result.getResults().size() > 0)
						checker.accept(ProjectValidate.INVALIDATE);
					else
						checker.accept(ProjectValidate.VALIDATE);
				} else {
					GWT.log("what happend?");
				}
			}, new HashSet<>(Arrays.asList(entity)));
		} else
			checker.accept(ProjectValidate.INVALIDATE);
	}

	private void upsertProject(String name, Map<String, String> descriptions, final String message) {
		ProjectEntity entity = new ProjectEntity();
		entity.setName(name);
		entity.setDescriptions(descriptions);
		ProjectAction action = new ProjectAction(new HashSet<>(Arrays.asList(entity)), ProjectAction.OP.UPSERT, null, 0, 1 /* one only */);
		sequence = Math.max(sequence, action.sequence);

		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				getView().Message(message);
				selectedProject = name;
				getView().showProjectView();
			}
		});
	}

	@Override
	public void updateProject(String name, Map<String, String> descriptions) {
		upsertProject(name, descriptions, "project updated");
	}

	@Override
	public void createProject(String name, Map<String, String> descriptions) {
		upsertProject(name, descriptions, "project created");
	}

	private void updateRoles(String account, String project, Set<String> roles, final String message) {
		RoleEntity entity = new RoleEntity();
		entity.addRoleSet(roles);
		RoleAction action = new RoleAction(account, project, entity);
		sequence = Math.max(sequence, action.sequence);

		dispatcher.execute(action, new AsyncCallback<GetResult<RoleEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResult<RoleEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				getView().Message(message);
			}
		});
	}

	@Override
	public void updateProjectRoleSet(String project, Set<String> roles) {
		updateRoles(null, project, roles, project + "角色更新");
	}

	@Override
	public void grantAccountRoles(String account, String project, Set<String> roles) {
		updateRoles(account, project, roles, "账户角色设定完成");
	}

	private final PlaceRequest accountSelectPlace = new PlaceRequest.Builder().nameToken(TokenNames.accounts)
			                                           .with(TokenNames.selector, getProxy().getNameToken()).build();
	@Override
	public void onAccountSelect() {
		placeManager.revealPlace(accountSelectPlace, false);
	}

	private final PlaceRequest projectSelectPlace = new PlaceRequest.Builder().nameToken(TokenNames.projects)
			                                       .with(TokenNames.selector, getProxy().getNameToken()).build();
	@Override
	public void onProjectSelect() {
		placeManager.revealPlace(projectSelectPlace, false);
	}

	@Override
	public void onProjectCreate() {
		getView().showCreateView();
	}

	@Override
	public void onProjectEdit() {
		getView().showEditView();
	}

	@Override
	public void onProjectRole() {
		getView().showRoleView();
	}

	@Override
	public void getProject(BiConsumer<String, Map<String, String>> project) {
		ProjectEntity entity = new ProjectEntity();
		entity.setName(selectedProject);
		projectsRead((caught , result) -> {
			if(null != caught)
				exceptionHandler.handler(caught);
			else if(null != result && (result.getResults().size() > 0)) {
				ProjectEntity e = result.getResults().get(0);
				project.accept(e.getName(), e.getDescriptions());
			} else {
				GWT.log("what happend?");
			}
		}, new HashSet<>(Arrays.asList(entity)));
	}

	@Override
	public void getProjectRoles(BiConsumer<String, Set<String>> role) {
		RoleAction action = new RoleAction(null, selectedProject);
		dispatcher.execute(action, new AsyncCallback<GetResult<RoleEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed: " + caught);
			}

			@Override
			public void onSuccess(GetResult<RoleEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				role.accept(selectedProject, result.getResults().getRoleSet());
			}
		});
	}
	
	@Override
	public void getRoles(BiConsumer<String, String> names, BiConsumer<Set<String>, Set<String>> roleSet) {
		if(null == selectedProject || null == selectedAccount) {
			names.accept(selectedAccount, selectedProject);
			return;
		}

		// Role Grant
		RoleAction projectAction = new RoleAction(null, selectedProject);
		RoleAction accountAction = new RoleAction(selectedAccount, selectedProject);
		dispatcher.execute(new UnsecuredSerializableBatchAction(OnException.ROLLBACK, projectAction, accountAction),
			new AsyncCallback<GetResults<GetResult<? extends IsSerializable>>>() {
				@Override
				public void onFailure(Throwable caught) {
					exceptionHandler.handler(caught);
				}

				@Override
				public void onSuccess(GetResults<GetResult<?>> result) {
					List<GetResult<?>> results = result.getResults();
						RoleEntity projectRoleSet = (RoleEntity) (((GetResult<?>) results.get(0).getResults()).getResults());
						assert null != projectRoleSet.getRoleSet();
						RoleEntity accountRoleSet = (RoleEntity) (((GetResult<?>) results.get(1).getResults()).getResults());
						assert null != accountRoleSet.getRoleSet();
						names.accept(selectedAccount, selectedProject);
						roleSet.accept(accountRoleSet.getRoleSet(), projectRoleSet.getRoleSet());
				};
			});	
	}
}
