package ${package}.presentermodules.home.cards.simple;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SimpleWidgetPresenter extends PresenterWidget<SimpleWidgetPresenter.MyView> implements MyUiHandlers {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
	}

	@Inject
	public SimpleWidgetPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);
		getView().setUiHandlers(this);
	}
}
