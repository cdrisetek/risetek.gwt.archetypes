package ${package}.presentermodules.projects.list;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.Inject;
import ${package}.share.auth.projects.ProjectEntity;
import ${package}.ui.infinitycard.CardInfinityView;
import ${package}.ui.infinitycard.CardWidget;
import ${package}.ui.infinitycard.IsCardWidget;

import gwt.material.design.client.ui.MaterialValueBox;

public class ProjectCardInfinityView extends CardInfinityView<ProjectEntity, ProjectCardInfinityView.Card> {
	interface Binder extends UiBinder<HTMLPanel, ProjectCardInfinityView> {}
	@Inject
	public ProjectCardInfinityView(Binder binder) {
		super();
		binder.createAndBindUi(this);
	}

	interface CardListStyle extends CssResource {
		String card_removing();
		String card_removed();
		String card_transition();
	}
	
	@UiField CardListStyle style;
	@UiField HTMLPanel editor, creator;

	@UiField MaterialValueBox<String> editorNote, creatorNote, creatorName;

	@Override
	public IsWidget getDialogContent(IsCardWidget<ProjectEntity> card) {
		if(null == card) {
			creatorName.clearErrorText();
			creatorName.clear();
			creatorName.setFocus(true);
			return creator;
		}
		editorNote.setValue(card.getEntity().getNote());
		editorNote.setFocus(true);
		return editor;
	}

	@Override
	public boolean checkCommitValid(IsCardWidget<ProjectEntity> card) {
		if(null == card && creatorName.getValue().isEmpty()) {
			creatorName.setErrorText("Project Name can not be empty.");
			Scheduler.get().scheduleFixedDelay(()->{creatorName.clearErrorText(); creatorName.setFocus(true);return false;}, 500);
			return false;
		}
		return true;
	}
	
	@Override
	public ProjectEntity getDialogEntity(IsCardWidget<ProjectEntity> card) {
		ProjectEntity e;
		if(null == card) {
			e = new ProjectEntity();
			e.setName(creatorName.getValue());
			e.setNote(creatorNote.getValue());
		} else {
			e = card.getEntity(); 
			e.setNote(editorNote.getValue());
		}
		return e;
	}
	
	@Override
	public void setDialogFocus(IsCardWidget<ProjectEntity> card) {
		if(null == card)
			creatorName.setFocus(true);
		else
			editorNote.setFocus(true);
	}

	// NOTE: delay instance for builder because uihandlers have not set.
	private Builder builder;

	public IsCardWidget<ProjectEntity> BuildCard(ProjectEntity entity) {
		if(null == builder)
			builder = new Builder();
			
		return builder.setData(entity).build();
	}
	
	class Builder extends CardWidget.Builder<ProjectEntity> {
		public Builder() {
			super(getUiHandlers());
		}

		public CardWidget<ProjectEntity> build() {
			return super.build(new Card());
		}
	}

	@UiTemplate("ProjectCard.ui.xml")
	interface CardBinder extends UiBinder<HTMLPanel, Card> {
		CardBinder uiBinder = GWT.create(CardBinder.class);
	}
	class Card extends CardWidget<ProjectEntity> {
		@UiField HTML name, note;
		
		public Card() {
			initWidget(CardBinder.uiBinder.createAndBindUi(Card.this));
		}

		@Override
		public void deleteCard() {
			asWidget().addStyleName(style.card_removing());
			Set<IsCardWidget<ProjectEntity>> cards = new HashSet<>();
			cards.add(this);
			uiHandlers.onDeleteCards(cards);
			asWidget().addStyleName(style.card_removed());
		}
		
		@Override
		public void render() {
			name.setHTML(entity.getName());
			note.setHTML(entity.getNote());
			note.setTitle(entity.getNote());
		}
/*
		@UiHandler("delete")
		void onDeleteClick(ClickEvent e) {
			deleteCard();
		}
*/

		@UiHandler("btnForward")
		void onForwardClick(ClickEvent e) {
			uiHandlers.gotoPlace(getEntity());
			// uiHandlers.openCardDialog(this);
		}

		@UiHandler("edit")
		void onEditClick(ClickEvent e) {
			uiHandlers.openCardDialog(this);
		}

		public void setTransition(UIObject object) {
			object.addStyleName(style.card_transition());
			Scheduler.get().scheduleFixedDelay(()->{object.removeStyleName(style.card_transition()); return false;}, 500);
		}

		@Override
		public void updateCard(ProjectEntity entity) {
			if(!note.getHTML().equals(entity.getNote())) {
				setTransition(note);
			}
			render();
		}
	}
}
