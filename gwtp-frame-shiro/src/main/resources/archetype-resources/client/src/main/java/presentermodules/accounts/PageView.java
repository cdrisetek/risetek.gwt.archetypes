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
import ${package}.utils.SheetField.TYPE;

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
	@UiField HTMLPanel panelSlot;
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
		bindSlot(SLOT, panelSlot);
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

	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		if(accountView.asWidget().isAttached()) {
			getUiHandlers().onGoBackPlace();
			return;
		}
		setInSlot(SLOT, accountView);
	}

	static class CreateView extends ViewWithUiHandlers<MyUiHandlers> {
		@UiField HTMLPanel panelValidate, iconChecking, iconValidate, iconInvalidate;
		@UiField MaterialButton btnCommit;
		@UiField MaterialValueBox<String> boxName, boxPassword, boxPassword2, boxEmail, boxTelphone, boxNotes;
		interface Binder extends UiBinder<Widget, CreateView> {}

		private final SheetField fieldHeader;
		@Inject
		public CreateView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
			// Used to hold icons to indicate account name verification status.
			boxName.add(panelValidate);
			boxName.getValueBoxBase().getElement().setAttribute("spellcheck", "false");

			// Build validation chain.
			(fieldHeader = new SheetField.Builder(boxName).set(isStop -> {
    			getUiHandlers().checkValidate(boxName.getValue(), (state) -> {
        			setValidateState(state);
        			if(state == AccountValidate.CHECKING)
        				return;
        			if(state == AccountValidate.VALIDATE)
        				isStop.accept(false);
        			else {
        				btnCommit.setEnabled(false);
        				isStop.accept(true);
        			}});
        	}).checkKeyPress().build())
			.nextField(boxPassword).minLength(4).build()
			.nextField(boxPassword2).set(isStop -> {
				if(!boxPassword.getValue().equals(boxPassword2.getValue())) {
					boxPassword2.setFocus(true);
					isStop.accept(true);
				} else {
	    			btnCommit.setEnabled(true);
					isStop.accept(false);
				}
    		}).build()
	        .nextField(boxEmail).type(TYPE.EMAIL).build()
	        .nextField(boxTelphone).type(TYPE.TELPHONE).build()
	        .nextField(boxNotes).build()
	        .nextField(btnCommit).build();
		}

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(boxEmail.getValue()).ifPresent(v->descriptions.put(EnumAccount.EMAIL.name(), v));
			Optional.ofNullable(boxTelphone.getValue()).ifPresent(v->descriptions.put(EnumAccount.TELPHONE.name(), v));
			Optional.ofNullable(boxNotes.getValue()).ifPresent(v->descriptions.put(EnumAccount.NOTES.name(), v));
			
			getUiHandlers().createAccount(boxName.getValue(), boxPassword.getValue(), descriptions);
		}
		
		@UiHandler("boxPassword")
		void onPasswordFocus(FocusEvent e) {
			fieldHeader.validate(boxPassword);
		}

		@UiHandler("boxPassword2")
		void onPassword2Focus(FocusEvent e) {
			fieldHeader.validate(boxPassword2);
		}

		@UiHandler("boxEmail")
		void onEmailFocus(FocusEvent e) {
			fieldHeader.validate(boxEmail);
		}

		@UiHandler("boxTelphone")
		void onTelphoneFocus(FocusEvent e) {
			fieldHeader.validate(boxTelphone);
		}

		@UiHandler("boxNotes")
		void onNotesFocus(FocusEvent e) {
			fieldHeader.validate(boxNotes);
		}
		
		@Override
		protected void onAttach() {
			super.onAttach();
			panelValidate.clear();
			boxName.clear();
			boxPassword.clear();
			boxPassword2.clear();
			boxEmail.clear();
			boxNotes.clear();
			btnCommit.setEnabled(false);
	        boxName.setFocus(true);
			
			Scheduler.get().scheduleDeferred(() -> {
				// Set Icon box as the same height as input box to stay center. 
		        panelValidate.getElement().getStyle().setWidth(boxName.asValueBoxBase().getOffsetHeight(), Unit.PX);
		        panelValidate.getElement().getStyle().setHeight(boxName.asValueBoxBase().getOffsetHeight(), Unit.PX);
			});
		}
		
		private void setValidateState(AccountValidate state) {
			panelValidate.clear();
			switch(state) {
			case UNKNOWN:
				btnCommit.setEnabled(false);
				break;
			case CHECKING:
       			panelValidate.add(iconChecking);
				break;
			case VALIDATE:
       			panelValidate.add(iconValidate);
				break;
			case INVALIDATE:
       			panelValidate.add(iconInvalidate);
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
			// Build validation chain.
			(fieldHeader = new SheetField.Builder(boxEmail).type(TYPE.EMAIL).build())
	        .nextField(boxTelphone).type(TYPE.TELPHONE).build()
	        .nextField(boxNotes).build()
	        .nextField(btnCommit).build();
		}
		
		@UiField MaterialValueBox<String> boxName, boxEmail, boxTelphone, boxNotes;
		@UiField MaterialButton btnEnable, btnDisable, btnCommit;
		private final SheetField fieldHeader;

		@UiHandler("boxEmail")
		void onEmailFocus(FocusEvent e) {
			fieldHeader.validate(boxEmail);
		}

		@UiHandler("boxTelphone")
		void onTelphoneFocus(FocusEvent e) {
			fieldHeader.validate(boxTelphone);
		}

		@UiHandler("boxNotes")
		void onNotesFocus(FocusEvent e) {
			fieldHeader.validate(boxNotes);
		}

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(boxEmail.getValue()).ifPresent(v->descriptions.put(EnumAccount.EMAIL.name(), v));
			Optional.ofNullable(boxTelphone.getValue()).ifPresent(v->descriptions.put(EnumAccount.TELPHONE.name(), v));
			Optional.ofNullable(boxNotes.getValue()).ifPresent(v->descriptions.put(EnumAccount.NOTES.name(), v));
			descriptions.put(EnumAccount.STATUS.name(), btnEnable.isEnabled()?"disable":null);
			
			getUiHandlers().updateAccount(boxName.getValue(), descriptions);
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
				boxName.setValue(name);
				boxEmail.setValue(descriptions.get(EnumAccount.EMAIL.name()));
				boxTelphone.setValue(descriptions.get(EnumAccount.TELPHONE.name()));
				boxNotes.setValue(descriptions.get(EnumAccount.NOTES.name()));
				String status = descriptions.get(EnumAccount.STATUS.name());
				boolean s = null != status && status.equals("disable");
				toggleStatus(!s);
				Scheduler.get().scheduleDeferred(() -> {
					boxEmail.setFocus(true);
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
		
		@UiField MaterialChip labSelectedAccount;
		@UiField MaterialLink btnAccountSet;
		
		@UiField MaterialRow panelRoles, panelProjectRoles;
		@UiField MaterialCardContent panelRolesContent;
		private Set<String> accountRoleSet, projectRoleSet;
		
		@UiHandler("btnAccountSelect")
		void onAccountSelect(ClickEvent e) {
			getUiHandlers().onAccountSelect();
		}

		@UiHandler("btnAccountCreate")
		void onAccountCreate(ClickEvent e) {
			getUiHandlers().onAccountCreate();
		}

		@UiHandler("btnAccountSet")
		void onAccountSet(ClickEvent e) {
			getUiHandlers().onAccountEdit();
		}
		
		private void updateRoles() {
			renderAccountRoles();
			renderHostProjectRoles();
			getUiHandlers().updateAccountRoles(accountRoleSet);
		}
		
		private void renderHostProjectRoles() {
			panelProjectRoles.clear();
			if(null == projectRoleSet)
				return;
			for(String role:projectRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.add(l.getText());
					projectRoleSet.remove(l.getText());
					updateRoles();
				});
				panelProjectRoles.add(chip);
			}
		}

		private void renderAccountRoles() {
			panelRoles.clear();
			if(null == accountRoleSet)
				return;
			for(String role:accountRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.remove(l.getText());
					projectRoleSet.add(l.getText());
					updateRoles();
				});
				panelRoles.add(chip);
			}
		}
		
		@Override
		protected void onAttach() {
			labSelectedAccount.setVisible(false);
			btnAccountSet.setEnabled(false);
			accountRoleSet = null;
			projectRoleSet = null;
			panelRolesContent.setVisible(false);
			renderAccountRoles();
			renderHostProjectRoles();

			getUiHandlers().getAccountRoles((name) -> {
				labSelectedAccount.setValue(name);
				labSelectedAccount.setVisible((null != name && !name.isEmpty()));
				btnAccountSet.setEnabled((null != name && !name.isEmpty()));
			}, (roles, projectRoles) -> {
				accountRoleSet = roles;
				projectRoleSet = projectRoles;

				boolean visible = (null != projectRoleSet) && (!projectRoleSet.isEmpty());
				panelRolesContent.setVisible(visible);

				for(String s:accountRoleSet)
					projectRoleSet.remove(s);
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
