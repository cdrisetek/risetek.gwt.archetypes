package ${package}.presentermodules.security;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import ${package}.share.accounts.EnumAccount;
import ${package}.utils.SheetField;
import ${package}.utils.SheetField.TYPE;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialValueBox;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	interface Binder extends UiBinder<Widget, PageView> {}
	private final Slot<PresenterWidget<?>> SLOT = new Slot<>();

	@UiField Panel panelSlot;
	
	@Inject
	public PageView(final Binder uiBinder,
	                final PasswordView passwordView,
	                final EmailView emailView,
			        final AccountView accountView
			) {
		initWidget(uiBinder.createAndBindUi(this));

		this.passwordView = passwordView;
		this.emailView = emailView;
		this.accountView = accountView;
		bindSlot(SLOT, panelSlot);

		Scheduler.get().scheduleDeferred(() -> {
			assert null != getUiHandlers() : "null uiHandlers.";
			this.passwordView.setUiHandlers(getUiHandlers());
			this.emailView.setUiHandlers(getUiHandlers());
			this.accountView.setUiHandlers(getUiHandlers());
		});
	}

	@UiHandler("btnGoback")
	public void onGoback(ClickEvent e) {
		if(accountView.asWidget().isAttached())
			getUiHandlers().onGoBackPlace();
		else
			setInSlot(SLOT, accountView);
	}

	@Override
	public void showPasswordView() {
		setInSlot(SLOT, passwordView);
	}
	
	static class PasswordView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, PasswordView> {}
		@UiField MaterialValueBox<String> boxPassword, boxPassword2;
		@UiField MaterialButton btnCommit;

		@Inject
		public PasswordView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
			new SheetField.Builder(boxPassword).minLength(4).build()
			.nextField(boxPassword2).set(isStop -> {
				if(!boxPassword.getValue().equals(boxPassword2.getValue())) {
					boxPassword2.setFocus(true);
	    			btnCommit.setEnabled(false);
					isStop.accept(true);
				} else {
	    			btnCommit.setEnabled(true);
					isStop.accept(false);
				}
    		}).checkKeyPress().build()
			.nextField(btnCommit).build();
		}

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			getUiHandlers().updatePassword(boxPassword.getValue());
		}

		@Override
		public void onAttach() {
			btnCommit.setEnabled(false);
			boxPassword.clear();
			boxPassword2.clear();
			boxPassword.setFocus(true);
		}
	}
	private final PasswordView passwordView;

	
	@Override
	public void showEmailView() {
		setInSlot(SLOT, emailView);
	}
	
	static class EmailView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, EmailView> {}
		@UiField MaterialValueBox<String> boxEmail;
		@UiField MaterialButton btnCommit;

		@Inject
		public EmailView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
			
			Element e = boxEmail.getValueBoxBase().getElement(); 
			e.setAttribute("spellcheck", "false");
			e.setAttribute("autocapitalize", "off");
			e.setAttribute("autocomplete", "off");
			e.setAttribute("AutoCompleteType", "Disabled");
			e.setAttribute("autocorrect", "off");

			new SheetField.Builder(boxEmail).type(TYPE.EMAIL).minLength(1).checkKeyPress().set(isStoped->{
				// here we go, the field had validate by EMAIL pattern and not empty, so it is validation.
				btnCommit.setEnabled(true);
				isStoped.accept(false);
			}).build()
			.nextField(btnCommit).checkOnFocus().build();
		}

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			getUiHandlers().updateEmail(boxEmail.getValue());
		}

		@Override
		public void onAttach() {
			boxEmail.setText(getUiHandlers().getSecurityInformation(EnumAccount.EMAIL.name()));
//			boxEmail.clear();
			btnCommit.setEnabled(false);
			boxEmail.setFocus(true);
			boxEmail.select();
		}
	}
	private final EmailView emailView;

	@Override
	public void showAccountView() {
		setInSlot(SLOT, accountView);
	}
	
	static class AccountView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, AccountView> {}

		@UiField Label labAccount, labEmail;
	
		@Inject
		public AccountView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
		}

		@UiHandler("btnPassword")
		public void onPasswordClick(ClickEvent e) {
			getUiHandlers().showPasswordView();
		}

		@UiHandler("btnEmail")
		public void onEmailClick(ClickEvent e) {
			getUiHandlers().showEmailView();
		}

		@Override
		public void onAttach() {
			labAccount.setText(getUiHandlers().getSecurityInformation(null /* for principal */));
			labEmail.setText(getUiHandlers().getSecurityInformation(EnumAccount.EMAIL.name()));
		}
	}
	private final AccountView accountView;
}
