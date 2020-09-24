package ${package}.presentermodules.projects.project;

import java.util.HashSet;
import java.util.Optional;
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
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.presentermodules.projects.TokenNames;
import ${package}.root.RootPresenter;
import ${package}.share.GetResults;
import ${package}.share.projects.ProjectAction;
import ${package}.share.projects.ProjectEntity;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		public void showProject(ProjectEntity entity);
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

	private long sequence = 0, lastsequence = 0;
	public void onLoadRange(Set<ProjectEntity> entities) {
		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.READ, null,
			                                     0, 0, sequence++);

		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				if (action.sequence < lastsequence) {
					GWT.log("discard action: " + action.sequence);
					return;
				}

				Optional.ofNullable(result.getResults()).ifPresent(l->{
					getView().showProject(l.get(0));
					});
				lastsequence = action.sequence;
			}
		});
	}
	
	@Override
	public void onReveal() {
		String name = placeManager.getCurrentPlaceRequest().getParameter("name", null);
		if(name == null)
			exceptionHandler.handler(new Throwable("access project with null"));

		ProjectEntity entity = new ProjectEntity();
		entity.setName(name);
		Set<ProjectEntity> entities = new HashSet<>();
		entities.add(entity);
		onLoadRange(entities);
	}

	private final PlaceRequest placeRequest = new Builder().nameToken(TokenNames.projects).build();
	@Override
	public void gotoPlaceProjects() {
		placeManager.revealPlace(placeRequest);
		// TODO Auto-generated method stub
		
	}
}
