package ${package}.presentermodules.accounts.selector;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.ui.infinitycard.CardInfinityView;
import ${package}.ui.infinitycard.CardWidget;
import ${package}.ui.infinitycard.IsCardWidget;

public class SelectorView extends CardInfinityView<AccountEntity, SelectorView.Card>
                          implements SelectorWidget.MyView {
	interface Binder extends UiBinder<Widget, SelectorView> {}
	@Inject
	public SelectorView(Binder binder) {
		binder.createAndBindUi(this);
	}
	
	@Override
	public IsCardWidget<AccountEntity> buildCard(AccountEntity entity) {
		return new Card(entity);
	}

	@UiTemplate("Card.ui.xml")
	interface CardBinder extends UiBinder<Widget, Card> {
		CardBinder uiBinder = GWT.create(CardBinder.class);
	}
	protected class Card extends CardWidget<AccountEntity> {
		@UiField HTML name, note, email, telphone;
		
		public Card(AccountEntity entity) {
			initWidget(CardBinder.uiBinder.createAndBindUi(this));
			this.entity = entity;
			render();
		}
		
		private void render() {
			name.setHTML(entity.getPrincipal());
			Map<String, String> description = entity.getDescriptions();
			email.setHTML(description.get(EnumAccount.EMAIL.name()));
			telphone.setHTML(description.get(EnumAccount.TELPHONE.name()));
			note.setHTML(description.get(EnumAccount.NOTES.name()));
		}
		
		@UiHandler("cardPanel")
		public void onCardPanelClick(ClickEvent e) {
			getUiHandlers().onSelected(this);
		}
	}
}
