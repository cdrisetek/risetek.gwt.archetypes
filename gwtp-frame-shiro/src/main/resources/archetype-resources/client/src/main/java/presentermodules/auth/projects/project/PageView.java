package ${package}.presentermodules.auth.projects.project;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.share.auth.projects.ProjectEntity;

import gwt.material.design.client.ui.MaterialValueBox;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	interface Binder extends UiBinder<HTMLPanel, PageView> {}

	@UiField HTML name;
	@UiField MaterialValueBox<String> note;
	@UiField HTMLPanel roleCards;
	
	
	@Inject
	public PageView(final EventBus eventBus, final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void showProject(ProjectEntity entity) {
		name.setHTML(entity.getName());
		note.setValue(entity.getNote());
		roleCards.clear();
		entity.getRoleSet().forEach(r->{roleCards.add(new HTML(r));});
	}
	
	@UiHandler("btnBack")
	void onBackClick(ClickEvent e) {
		getUiHandlers().gotoPlaceProjects();
	}
}
