package ${package}.presentermodules.accounts.projects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
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
import ${package}.presentermodules.accounts.projects.MyUiHandlers.ProjectValidate;
import ${package}.share.accounts.projects.EnumProject;
import ${package}.utils.SheetField;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialButton;
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
			        final RoleView roleView,
			        final ProjectView projectView,
			        final CreateView createView) {
		initWidget(uiBinder.createAndBindUi(this));
		this.createView = createView;
		this.editView = editView;
		this.roleView = roleView;
		this.projectView = projectView;
		bindSlot(SLOT, panelSlot);
		Scheduler.get().scheduleDeferred(()-> {
			assert(null != getUiHandlers());
			this.createView.setUiHandlers(getUiHandlers());
			this.editView.setUiHandlers(getUiHandlers());
			this.roleView.setUiHandlers(getUiHandlers());
			this.projectView.setUiHandlers(getUiHandlers());
		});
	}
	
	@Override
	public void Message(String message) {
		MaterialToast.fireToast(message);
	}

	@UiHandler("btnGoback")
	public void onGobackClick(ClickEvent e) {
		if(projectView.asWidget().isAttached()) {
			getUiHandlers().onGoBackPlace();
			return;
		}
		setInSlot(SLOT, projectView);
	}

	static class CreateView extends ViewWithUiHandlers<MyUiHandlers> {
		@UiField HTMLPanel panelValidate, iconChecking, iconValidate, iconInvalidate;
		interface Binder extends UiBinder<Widget, CreateView> {}
		private final SheetField fields;
		@Inject
		public CreateView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
	        boxName.add(panelValidate);
			boxName.getValueBoxBase().getElement().setAttribute("spellcheck", "false");

			// Build validation chain.
			(fields = new SheetField.Builder(boxName).set(isStop -> {
    			getUiHandlers().checkValidate(boxName.getValue(), (state) -> {
        			setValidateState(state);
        			if(state == ProjectValidate.CHECKING)
        				return;
        			if(state == ProjectValidate.VALIDATE)
        				isStop.accept(false);
        			else {
        				btnCommit.setEnabled(false);
        				isStop.accept(true);
        			}});
        	}).checkKeyPress().build())
			.nextField(boxSecret).minLength(8).build()
	        .nextField(boxNotes).build()
	        .nextField(btnCommit).build();
		}
		
		@UiField MaterialButton btnCommit;
		@UiField MaterialValueBox<String> boxName, boxNotes, boxSecret;

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(boxNotes.getValue()).ifPresent(v->descriptions.put(EnumProject.NOTES.name(), v));
			Optional.ofNullable(boxSecret.getValue()).ifPresent(v->descriptions.put(EnumProject.SECRET.name(), v));
			getUiHandlers().createProject(boxName.getValue(), descriptions);
		}

		@UiHandler("boxNotes")
		void onNotesFocus(FocusEvent e) {
			fields.validate(boxNotes);
		}
		
		@UiHandler("boxSecret")
		void onSecretFocus(FocusEvent e) {
			fields.validate(boxNotes);
		}

		@Override
		protected void onAttach() {
			super.onAttach();
			panelValidate.clear();
			boxName.clear();
			boxNotes.clear();
			boxSecret.clear();
			boxName.setFocus(true);

			Scheduler.get().scheduleDeferred(() -> {
				// Set Icon box as the same height as input box to stay center. 
		        panelValidate.getElement().getStyle().setWidth(boxName.asValueBoxBase().getOffsetHeight(), Unit.PX);
		        panelValidate.getElement().getStyle().setHeight(boxName.asValueBoxBase().getOffsetHeight(), Unit.PX);
			});
		}
		
		private void setValidateState(ProjectValidate state) {
			panelValidate.clear();
			switch(state) {
			case EMPTY:
				GWT.log("ProjectValidate empty");
				btnCommit.setEnabled(false);
				break;
			case CHECKING:
       			panelValidate.add(iconChecking);
				break;
			case VALIDATE:
       			panelValidate.add(iconValidate);
				btnCommit.setEnabled(true);
				break;
			case INVALIDATE:
       			panelValidate.add(iconInvalidate);
				break;
			default:
				GWT.log("ProjectValidate: " + state + " not handler");
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
		private final SheetField fields;
		@Inject
		public EditView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));

			// Build validation chain.
			(fields = new SheetField.Builder(boxSecret).minLength(8).build())
	        .nextField(boxNotes).build()
	        .nextField(btnCommit).build();
		}

		@UiField MaterialValueBox<String> boxName, boxNotes, boxSecret;
		@UiField MaterialButton btnEnable, btnDisable;
		@UiField MaterialButton btnCommit;

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(boxNotes.getValue()).ifPresent(v->descriptions.put(EnumProject.NOTES.name(), v));
			Optional.ofNullable(boxSecret.getValue()).ifPresent(v->descriptions.put(EnumProject.SECRET.name(), v));
			if(btnEnable.isEnabled()) descriptions.put(EnumProject.STATUS.name(), "disable");
			
			getUiHandlers().updateProject(boxName.getValue(), descriptions);
		}
		
		@UiHandler("boxNotes")
		void onNotesFocus(FocusEvent e) {
			fields.validate(boxNotes);
		}
		
		@UiHandler("boxSecret")
		void onSecretFocus(FocusEvent e) {
			fields.validate(boxNotes);
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
			super.onAttach();

			getUiHandlers().getProject((name, descriptions) -> {
				boxName.setValue(name);
				boxSecret.setValue(descriptions.get(EnumProject.SECRET.name()));
				boxNotes.setValue(descriptions.get(EnumProject.NOTES.name()));
				String status = descriptions.get(EnumProject.STATUS.name());
				boolean s = "disable".equals(status);
				toggleStatus(!s);
			});
			boxSecret.setFocus(true);
		}
	}
	private final EditView editView;

	@Override
	public void showEditView() {
		setInSlot(SLOT, editView);
	}

	static class RoleView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, RoleView> {}
		@Inject
		public RoleView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
		}
		
		@UiField MaterialValueBox<String> boxRole;
		@UiField MaterialRow panelRoles;

		private String project;
		private Set<String> roleSet;

		private void render() {
			panelRoles.clear();
			for(String role:roleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					roleSet.remove(l.getText());
					getUiHandlers().updateProjectRoleSet(project, roleSet);
					render();
				});
				panelRoles.add(chip);
			}
		}

		@UiHandler("boxRole")
		public void onValueChange(ValueChangeEvent<?> e) {
			roleSet.add(boxRole.getValue().toUpperCase());
			boxRole.clear();
			getUiHandlers().updateProjectRoleSet(project, roleSet);
			render();
		}
		
		@Override
		protected void onAttach() {
			super.onAttach();

			getUiHandlers().getProjectRoles((name, roles) -> {
				project = name;
				roleSet = roles;
				render();
				Scheduler.get().scheduleDeferred(() -> boxRole.setFocus(true));
			});
		}
	}
	private final RoleView roleView;
	@Override
	public void showRoleView() {
		setInSlot(SLOT, roleView);
	}

	static class ProjectView extends ViewWithUiHandlers<MyUiHandlers> {
		interface Binder extends UiBinder<Widget, ProjectView> {}
		@Inject
		public ProjectView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
		}
		@UiField MaterialLink btnProjectSet, btnProjectRole;
		@UiField MaterialChip labSelectedAccount, labSelectedProject;
		@UiField Label labAccountRoles, labProjectRoles;
		@UiField MaterialWidget panelRolesContent, panelAccountRoles, panelProjectRoles;

		@UiHandler("btnAccountSelect")
		void onAccountSelect(ClickEvent e) {
			getUiHandlers().onAccountSelect();
		}

		@UiHandler("btnProjectCreate")
		void onProjectCreate(ClickEvent e) {
			getUiHandlers().onProjectCreate();
		}

		@UiHandler("btnProjectSelect")
		void onProjectSelect(ClickEvent e) {
			getUiHandlers().onProjectSelect();
		}

		@UiHandler("btnProjectSet")
		void onProjectSet(ClickEvent e) {
			getUiHandlers().onProjectEdit();
		}

		@UiHandler("btnProjectRole")
		void onProjectRole(ClickEvent e) {
			getUiHandlers().onProjectRole();
		}

		private void render() {
			panelAccountRoles.clear();
			for(String role:accountRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.remove(l.getText());
					projectRoleSet.add(l.getText());
					render();
					getUiHandlers().grantAccountRoles(labSelectedAccount.getValue(), labSelectedProject.getValue(), accountRoleSet);
				});
				panelAccountRoles.add(chip);
			}

			panelProjectRoles.clear();
			for(String role:projectRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.add(l.getText());
					projectRoleSet.remove(l.getText());
					render();
					getUiHandlers().grantAccountRoles(labSelectedAccount.getValue(), labSelectedProject.getValue(), accountRoleSet);
				});
				panelProjectRoles.add(chip);
			}
		}

		private Set<String> projectRoleSet, accountRoleSet;

		@Override
		protected void onAttach() {
			super.onAttach();

			panelRolesContent.setVisible(false);
			getUiHandlers().getRoles((account, project) -> {
				labSelectedAccount.setValue(account);
				labSelectedAccount.setVisible((null != account && !account.isEmpty()));
				labAccountRoles.setText("账户["+ account + "]设定的角色");
				labSelectedProject.setValue(project);
				labSelectedProject.setVisible((null != project && !project.isEmpty()));
				labProjectRoles.setText("项目["+ project + "]可分配的角色");
				btnProjectSet.setEnabled((null != project && !project.isEmpty()));
				btnProjectRole.setEnabled((null != project && !project.isEmpty()));
			}, (accountRoles, projectRoles) -> {
				accountRoleSet = accountRoles;
				projectRoleSet = projectRoles;
				
				// Remove roles from projectSet which accountSet already granted.
				for(String s:accountRoleSet)
					projectRoleSet.remove(s);

				panelRolesContent.setVisible(true);
				render();
			});
		}
	}
	private final ProjectView projectView;
	@Override
	public void showProjectView() {
		setInSlot(SLOT, projectView);
	}
}
