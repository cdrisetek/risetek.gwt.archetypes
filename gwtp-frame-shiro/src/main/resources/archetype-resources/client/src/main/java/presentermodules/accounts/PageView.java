package ${package}.presentermodules.accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import ${package}.presentermodules.accounts.MyUiHandlers.AccountValidate;
import ${package}.share.accounts.EnumAccount;
import ${package}.utils.SheetField;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialChip;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.MaterialValueBox;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
	interface Binder extends UiBinder<Widget, PageView> {}
	private final Slot<PresenterWidget<?>> SLOT = new Slot<>();
	@UiField HTMLPanel slot;
	@Inject
	public PageView(final EventBus eventBus,
			        final Binder uiBinder,
			        final EditView editView,
			        final AccountView accountView,
			        final CreateView createView) {
		initWidget(uiBinder.createAndBindUi(this));
		this.accountView = accountView;
		this.createView = createView;
		this.editView = editView;
		bindSlot(SLOT, slot);
		Scheduler.get().scheduleDeferred(()-> {
			assert(null != getUiHandlers());
			this.accountView.setUiHandlers(getUiHandlers());
			this.createView.setUiHandlers(getUiHandlers());
			this.editView.setUiHandlers(getUiHandlers());
		});
	}
	
	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}

	@UiHandler("lnkGoback")
	public void onGobackClick(ClickEvent e) {
		if(accountView.asWidget().isAttached()) {
			getUiHandlers().onGoBackPlace();
			return;
		}
		setInSlot(SLOT, accountView);
	}

	static class CreateView extends ViewWithUiHandlers<MyUiHandlers> {
		@UiField HTMLPanel boxValidate, iconChecking, iconValidate, iconInvalidate;
		@UiField MaterialButton btnCommit;
		@UiField MaterialValueBox<String> name, password, password2, email, telphone, notes;
		interface Binder extends UiBinder<Widget, CreateView> {}

		private final SheetField sfHeader;
		@Inject
		public CreateView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
			// Used to hold icons to indicate account name verification status.
			name.add(boxValidate);

			sfHeader = new SheetField.Builder(name).set(stayPoint -> {
    			getUiHandlers().checkValidate(name.getValue(), (state) -> {
        			setAccountValidateState(state);
        			if(state == AccountValidate.CHECKING)
        				return;
        			if(state == AccountValidate.VALIDATE)
        				stayPoint.accept(false);
        			else {
        				name.setFocus(true);
        				btnCommit.setEnabled(false);
        				stayPoint.accept(true);
        			}});
        	}).doValidator().build();

			sfHeader.nextBuilder(password).set(stayPoint -> {
				if(!isPasswordValid()) {
					password.setFocus(true);
					stayPoint.accept(true);
				} else
					stayPoint.accept(false);
	        }).build().nextBuilder(password2).set(stayPoint -> {
				if(!password.getValue().equals(password2.getValue())) {
					password2.setFocus(true);
					stayPoint.accept(true);
				} else {
	    			btnCommit.setEnabled(true);
					stayPoint.accept(false);
				}
    		}).build()
	        .nextBuilder(email).build()
	        .nextBuilder(telphone).build()
	        .nextBuilder(notes).build()
	        .nextBuilder(btnCommit).build();
		}

		private boolean isPasswordValid() {
			if(password.getValue().isEmpty())
				return false;
			
			if(password.getValue().length() < 4)
				return false;
			return true;
		}

		private void check(Widget target) {
			sfHeader.nestedCheck(target);
		}

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(email.getValue()).ifPresent(v->descriptions.put(EnumAccount.EMAIL.name(), v));
			Optional.ofNullable(telphone.getValue()).ifPresent(v->descriptions.put(EnumAccount.TELPHONE.name(), v));
			Optional.ofNullable(notes.getValue()).ifPresent(v->descriptions.put(EnumAccount.NOTES.name(), v));
			
			getUiHandlers().createAccount(name.getValue(), password.getValue(), descriptions);
		}
		
		@UiHandler("password")
		void onPasswordFocus(FocusEvent e) {
			check(password);
		}

		@UiHandler("password2")
		void onPassword2Focus(FocusEvent e) {
			check(password2);
		}

		@UiHandler("email")
		void onEmailFocus(FocusEvent e) {
			check(email);
		}

		@UiHandler("telphone")
		void onTelphoneFocus(FocusEvent e) {
			check(telphone);
		}

		@UiHandler("notes")
		void onNotesFocus(FocusEvent e) {
			check(notes);
		}
		
		@Override
		protected void onAttach() {
			super.onAttach();
			name.clear();
			boxValidate.clear();
			password.clear();
			password2.clear();
			notes.clear();
			Scheduler.get().scheduleDeferred(() -> {
				// Set Icon box as the same height as input box to stay center. 
		        boxValidate.getElement().getStyle().setWidth(name.asValueBoxBase().getOffsetHeight(), Unit.PX);
		        boxValidate.getElement().getStyle().setHeight(name.asValueBoxBase().getOffsetHeight(), Unit.PX);
		        name.setFocus(true);
			});
		}
		
		private void setAccountValidateState(AccountValidate state) {
			boxValidate.clear();
			switch(state) {
			case UNKNOWN:
				btnCommit.setEnabled(false);
				break;
			case CHECKING:
       			boxValidate.add(iconChecking);
				break;
			case VALIDATE:
       			boxValidate.add(iconValidate);
				break;
			case INVALIDATE:
       			boxValidate.add(iconInvalidate);
				break;
			default:
				assert false;
				break;
			}
		}
	}
	private final CreateView createView;

	@Override
	public void showCreateView() {
		setInSlot(SLOT, createView);
	}
	
	static class EditView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, EditView> {}
		@Inject
		public EditView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
			sfHeader = new SheetField.Builder(email).build();
	        sfHeader.nextBuilder(telphone).build()
	        .nextBuilder(notes).build()
	        .nextBuilder(btnCommit).build();
		}
		
		@UiField MaterialValueBox<String> name, email, telphone, notes;
		@UiField MaterialButton btnEnable, btnDisable, btnCommit;
		private final SheetField sfHeader;

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			// TODO: check valid
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(email.getValue()).ifPresent(v->descriptions.put(EnumAccount.EMAIL.name(), v));
			Optional.ofNullable(telphone.getValue()).ifPresent(v->descriptions.put(EnumAccount.TELPHONE.name(), v));
			Optional.ofNullable(notes.getValue()).ifPresent(v->descriptions.put(EnumAccount.NOTES.name(), v));
			descriptions.put(EnumAccount.STATUS.name(), btnEnable.isEnabled()?"disable":null);
			
			getUiHandlers().updateAccount(name.getValue(), descriptions);
		}
		
		@UiHandler("btnEnable")
		public void onEnableClick(ClickEvent e) {
			toggleStatus(true);
		}

		@UiHandler("btnDisable")
		public void onDisableClick(ClickEvent e) {
			toggleStatus(false);
		}
		
		private void toggleStatus(boolean status) {
			btnDisable.setEnabled(status);
			btnEnable.setEnabled(!status);
		}
		
		@Override
		protected void onAttach() {
			getUiHandlers().getAccount((name, descriptions) -> {
				this.name.setValue(name);
				email.setValue(descriptions.get(EnumAccount.EMAIL.name()));
				telphone.setValue(descriptions.get(EnumAccount.TELPHONE.name()));
				notes.setValue(descriptions.get(EnumAccount.NOTES.name()));
				String status = descriptions.get(EnumAccount.STATUS.name());
				boolean s = null != status && status.equals("disable");
				toggleStatus(!s);
				Scheduler.get().scheduleDeferred(() -> {
					email.setFocus(true);
				});
			});
		}
	}
	private final EditView editView;

	@Override
	public void showEditView() {
		setInSlot(SLOT, editView);
	}

	static class AccountView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, AccountView> {}
		@Inject
		public AccountView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
		}
		
		@UiField MaterialChip selectedAccount;
		@UiField MaterialLink accountSet;
		
		@UiField MaterialRow panelRoles, hostProjectRoles;
		@UiField MaterialCardContent rolesContent;
		private Set<String> accountRoles, projectRoles;
		
		@UiHandler("accountSelect")
		void onAccountSelect(ClickEvent e) {
			getUiHandlers().onAccountSelect();
		}

		@UiHandler("accountCreate")
		void onAccountCreate(ClickEvent e) {
			getUiHandlers().onAccountCreate();
		}

		@UiHandler("accountSet")
		void onAccountSet(ClickEvent e) {
			getUiHandlers().onAccountEdit();
		}
		
		private void updateRoles() {
			renderAccountRoles();
			renderHostProjectRoles();
			getUiHandlers().updateAccountRoles(accountRoles);
		}
		
		private void renderHostProjectRoles() {
			hostProjectRoles.clear();
			for(String role:projectRoles) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoles.add(l.getText());
					projectRoles.remove(l.getText());
					updateRoles();
				});
				hostProjectRoles.add(chip);
			}
		}

		private void renderAccountRoles() {
			panelRoles.clear();
			for(String role:accountRoles) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoles.remove(l.getText());
					projectRoles.add(l.getText());
					updateRoles();
				});
				panelRoles.add(chip);
			}
		}
		
		@Override
		protected void onAttach() {
			getUiHandlers().getAccountRoles((name) -> {
				selectedAccount.setValue(name);
				selectedAccount.setVisible((null != name && !name.isEmpty()));
				accountSet.setEnabled((null != name && !name.isEmpty()));
			}, (roles, projectRoles) -> {
				accountRoles = roles;
				this.projectRoles = projectRoles;

				boolean visible = (null != this.projectRoles) && (!this.projectRoles.isEmpty());
				rolesContent.setVisible(visible);

				for(String s:accountRoles)
					this.projectRoles.remove(s);
				renderAccountRoles();
				renderHostProjectRoles();
			});
		}
	}
	private final AccountView accountView;

	@Override
	public void showAccountView() {
		setInSlot(SLOT, accountView);
	}
}
