package ${package}.presentermodules.accounts.projects.selector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import ${package}.share.accounts.projects.EnumProject;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.ui.infinitycard.CardInfinityView;
import ${package}.ui.infinitycard.CardUiHandlers;
import ${package}.ui.infinitycard.CardWidget;

public class SelectorView extends CardInfinityView<ProjectEntity, SelectorView.Card, CardUiHandlers<ProjectEntity>> 
	                      implements SelectorWidget.MyView {
	@Override
	public CardWidget<ProjectEntity> buildCard(ProjectEntity entity) {
		return new Card(entity);
	}
	
	@UiTemplate("Card.ui.xml")
	interface CardBinder extends UiBinder<Widget, Card> {
		CardBinder uiBinder = GWT.create(CardBinder.class);
	}
	protected class Card extends CardWidget<ProjectEntity> {
		@UiField HTML name, note;
		
		public Card(ProjectEntity entity) {
			super(entity);
			add(CardBinder.uiBinder.createAndBindUi(this));
			render();
			this.addClickHandler(e -> getUiHandlers().onSelected(this));
		}

		private void render() {
			name.setHTML(getEntity().getName());
			String notes = getEntity().getDescription(EnumProject.NOTES);
			note.setHTML(notes);
			note.setTitle(notes);
		}
	}
}
