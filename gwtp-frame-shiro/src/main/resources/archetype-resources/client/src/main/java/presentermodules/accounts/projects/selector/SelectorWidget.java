package ${package}.presentermodules.accounts.projects.selector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import ${package}.share.accounts.projects.ProjectAction;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.dispatch.GetResults;
import ${package}.ui.infinitycard.CardPresenterWidget;
import ${package}.ui.infinitycard.CardUiHandlers;
import ${package}.ui.infinitycard.LoadRange;
import ${package}.utils.ServerExceptionHandler;

public class SelectorWidget extends CardPresenterWidget<ProjectEntity, SelectorView.Card, CardUiHandlers<ProjectEntity>, SelectorView> {
	public interface MyView extends CardPresenterWidget.CardView<ProjectEntity, CardUiHandlers<ProjectEntity>> {
	}

	@Inject
	public SelectorWidget(
			final EventBus eventBus,
			final DispatchAsync dispatcher,
			final ServerExceptionHandler exceptionHandler,
			final SelectorView view) {
		super(eventBus, dispatcher, exceptionHandler, view);
		onRefresh();
	}

	private long lastsequence = 0;
	private boolean loading = false;
	
	@Override
	public void onLoadRange(final LoadRange loadRange) {
		if(loading) {
			// dataProvider.updateRowData(range.getStart(), new Vector<ProjectEntity>());
			return;
		}
		loading = true;
		ProjectAction action = new ProjectAction(null, ProjectAction.OP.READ, getSearchKey(),
				loadRange.loadRange.getStart(), loadRange.loadRange.getLength());

		lastsequence = Math.max(lastsequence, action.sequence);
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
				getView().updateRowData(loadRange.loadRange.getStart(), result.getResults(), loadRange);
			}
		});
	}
}
