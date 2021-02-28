package ${package}.presentermodules.accounts.projects.selector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import ${package}.share.accounts.projects.EnumProject;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.ui.infinitycard.CardInfinityView;
import ${package}.ui.infinitycard.CardWidget;
import ${package}.ui.infinitycard.IsCardWidget;

public class SelectorView extends CardInfinityView<ProjectEntity, SelectorView.Card> 
	                      implements SelectorWidget.MyView {
	interface Binder extends UiBinder<Widget, SelectorView> {}
	@Inject
	public SelectorView(Binder binder) {
		binder.createAndBindUi(this);
	}

	@Override
	public IsCardWidget<ProjectEntity> buildCard(ProjectEntity entity) {
		return new Card(entity);
	}
	
	@UiTemplate("Card.ui.xml")
	interface CardBinder extends UiBinder<Widget, Card> {
		CardBinder uiBinder = GWT.create(CardBinder.class);
	}
	protected class Card extends CardWidget<ProjectEntity> {
		@UiField HTML name, note;
		
		public Card(ProjectEntity entity) {
			initWidget(CardBinder.uiBinder.createAndBindUi(this));
			this.entity = entity;
			render();
		}

		@UiHandler("cardPanel")
		public void onCardPanelClick(ClickEvent e) {
			getUiHandlers().onSelected(this);
		}

		private void render() {
			name.setHTML(entity.getName());
			String notes = entity.getDescription(EnumProject.NOTES);
			note.setHTML(notes);
			note.setTitle(notes);
		}
	}
}
