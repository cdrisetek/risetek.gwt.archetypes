package ${package}.presentermodules.accounts.selector;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.ui.infinitycard.CardInfinityView;
import ${package}.ui.infinitycard.CardUiHandlers;
import ${package}.ui.infinitycard.CardWidget;

public class SelectorView extends CardInfinityView<AccountEntity, SelectorView.Card, CardUiHandlers<AccountEntity>>
                          implements SelectorWidget.MyView {
	@Override
	public CardWidget<AccountEntity> buildCard(AccountEntity entity) {
		return new Card(entity);
	}

	@UiTemplate("Card.ui.xml")
	interface CardBinder extends UiBinder<Widget, Card> {
		CardBinder uiBinder = GWT.create(CardBinder.class);
	}
	protected class Card extends CardWidget<AccountEntity> {
		@UiField HTML name, note, email, telphone;
		
		public Card(AccountEntity entity) {
			super(entity);
			add(CardBinder.uiBinder.createAndBindUi(this));
			render();
			this.addClickHandler(e -> getUiHandlers().onSelected(this));
		}
		
		private void render() {
			name.setHTML(getEntity().getPrincipal());
			Map<String, String> description = getEntity().getDescriptions();
			email.setHTML(description.get(EnumAccount.EMAIL.name()));
			telphone.setHTML(description.get(EnumAccount.TELPHONE.name()));
			note.setHTML(description.get(EnumAccount.NOTES.name()));
		}
	}
}
