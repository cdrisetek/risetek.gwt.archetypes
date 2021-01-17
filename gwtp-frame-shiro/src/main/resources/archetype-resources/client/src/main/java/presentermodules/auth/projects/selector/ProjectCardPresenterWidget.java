package ${package}.presentermodules.auth.projects.selector;

import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;
import ${package}.presentermodules.auth.TokenNames;
import ${package}.share.GetResults;
import ${package}.share.auth.projects.ProjectAction;
import ${package}.share.auth.projects.ProjectEntity;
import ${package}.ui.infinitycard.CardPresenterWidget;
import ${package}.ui.infinitycard.IsCardWidget;
import ${package}.utils.ServerExceptionHandler;

public class ProjectCardPresenterWidget extends CardPresenterWidget<ProjectEntity, ProjectCardInfinityView.Card> {
	private final PlaceManager placeManager;
	@Inject
	public ProjectCardPresenterWidget(
			EventBus eventBus,
			DispatchAsync dispatcher,
			final PlaceManager placeManager,
			ServerExceptionHandler exceptionHandler,
			ProjectCardInfinityView view) {
		super(eventBus, dispatcher, exceptionHandler, view);
		this.placeManager = placeManager;
	}

	private final Builder builder = new Builder();

	@Override
	public void gotoPlace(ProjectEntity project) {
		PlaceRequest placeRequest = builder.nameToken(TokenNames.project).with("name", project.getName()).build();
		GWT.log("goto:" + placeRequest.toString());
		placeManager.revealPlace(placeRequest, true);
	}
	
	private final AsyncDataProvider<ProjectEntity> dataProvider = new AsyncDataProvider<ProjectEntity>(
			new ProvidesKey<ProjectEntity>() {
				@Override
				public Object getKey(ProjectEntity item) {
					return item.getName();
				}
			}) {
		@Override
		protected void onRangeChanged(HasData<ProjectEntity> display) {
			onLoadRange(display.getVisibleRange());
		}
	};

	@Override
	protected AsyncDataProvider<ProjectEntity> getDataProvider() {
		return dataProvider;
	}		
		
	private long sequence = 0;
	private long lastsequence = 0;
	private boolean loading = false;
	
	@Override
	public void onLoadRange(Range range) {
		if(loading) {
			dataProvider.updateRowData(range.getStart(), new Vector<ProjectEntity>());
			return;
		}
		loading = true;
		ProjectAction action = new ProjectAction(null, ProjectAction.OP.READ, getSearchKey(),
			                                     range.getStart(), range.getLength(), sequence++);

		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				loading = false;
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				loading = false;
				if (action.sequence < lastsequence) {
					GWT.log("discard action: " + action.sequence);
					return;
				}

				lastsequence = action.sequence;
				dataProvider.updateRowData(range.getStart(), result.getResults());
			}
		});
	}

	@Override
	public void onDeleteCards(Set<IsCardWidget<ProjectEntity>> cards) {
		Set<ProjectEntity> entities = cards.stream().map(c->c.getEntity()).collect(Collectors.toSet());
		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.DELETE, null, 0, 0, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				cards.stream().forEach(c->getView().onCardRemove(c));
			}
		});
	}

	@Override
	public void onUpdateCards(Set<IsCardWidget<ProjectEntity>> cards) {
		Set<ProjectEntity> entities = cards.stream().map(c->c.getEntity()).collect(Collectors.toSet());
		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.UPSERT, null, 0, 0, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
				cards.stream().forEach(c->getView().onCardUpdate(c));
			}
		});
	}

	@Override
	public void onCreateEntities(Set<ProjectEntity> entities) {
		ProjectAction action = new ProjectAction(entities, ProjectAction.OP.UPSERT, null, 0, 0, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<ProjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<ProjectEntity> result) {
			}
		});
	}

	@Override 
	public String getDialogTitle(IsCardWidget<ProjectEntity> card) {
		return ((null == card)? "创建新项目":"修改项目:" + card.getEntity().getName());
	}
}
