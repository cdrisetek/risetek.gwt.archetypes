package ${package}.presentermodules.accounts.projects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
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
	@UiField HTMLPanel slot;
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
		bindSlot(SLOT, slot);
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

	@UiHandler("lnkGoback")
	public void onGobackClick(ClickEvent e) {
		if(projectView.asWidget().isAttached()) {
			getUiHandlers().onGoBackPlace();
			return;
		}
		setInSlot(SLOT, projectView);
	}

	static class CreateView extends ViewWithUiHandlers<MyUiHandlers> {
		@UiField HTMLPanel boxValidate, iconChecking, iconValidate, iconInvalidate;
		interface Binder extends UiBinder<Widget, CreateView> {}
		@Inject
		public CreateView(final EventBus eventBus, final Binder uiBinder) {
			initWidget(uiBinder.createAndBindUi(this));
	        name.add(boxValidate);
	        name.addKeyUpHandler(e -> {
	        	if(e.getNativeKeyCode() == KeyCodes.KEY_ENTER)
	        		notes.setFocus(true);
        		getUiHandlers().checkDuplicate(name.getValue(), state -> {
        			setAccountValidateState(state);
        		});
	        });
		}
		
		private boolean integrateCheck() {
    		return true;
		}

		@UiField MaterialButton btnCommit;
		@UiField MaterialValueBox<String> name, notes;

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			if(!integrateCheck())
				return;
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(notes.getValue()).ifPresent(v->descriptions.put(EnumProject.NOTES.name(), v));
			
			getUiHandlers().createProject(name.getValue(), descriptions);
		}
		
		@UiHandler("name")
		public void onNameBlur(BlurEvent e) {
    		getUiHandlers().checkDuplicate(name.getValue(), state -> setAccountValidateState(state));
		}
		
		@Override
		protected void onAttach() {
			super.onAttach();
			name.clear();
			boxValidate.clear();
			notes.clear();
			Scheduler.get().scheduleDeferred(() -> {
				name.setFocus(true);
				// Set Icon box as the same height as input box to stay center. 
		        boxValidate.getElement().getStyle().setWidth(name.asValueBoxBase().getOffsetHeight(), Unit.PX);
		        boxValidate.getElement().getStyle().setHeight(name.asValueBoxBase().getOffsetHeight(), Unit.PX);
			});
		}
		
		private void setAccountValidateState(ProjectValidate state) {
			boxValidate.clear();
			switch(state) {
			case EMPTY:
				GWT.log("ProjectValidate empty");
				btnCommit.setEnabled(false);
				break;
			case CHECKING:
       			boxValidate.add(iconChecking);
				break;
			case VALIDATE:
       			boxValidate.add(iconValidate);
				btnCommit.setEnabled(true);
				break;
			case INVALIDATE:
       			boxValidate.add(iconInvalidate);
				break;
			default:
				GWT.log("AccountValidate: " + state + " not handler");
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
		}
		
		@UiField MaterialValueBox<String> name, notes;
		@UiField MaterialButton btnEnable, btnDisable;

		@UiHandler("btnCommit")
		public void onCommitClick(ClickEvent e) {
			// TODO: check valid
			Map<String, String> descriptions = new HashMap<>();
			Optional.ofNullable(notes.getValue()).ifPresent(v->descriptions.put(EnumProject.NOTES.name(), v));
			if(btnEnable.isEnabled()) descriptions.put(EnumProject.STATUS.name(), "disable");
			
			getUiHandlers().updateProject(name.getValue(), descriptions);
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
				this.name.setValue(name);
				notes.setValue(descriptions.get(EnumProject.NOTES.name()));
				String status = descriptions.get(EnumProject.STATUS.name());
				boolean s = "disable".equals(status);
				toggleStatus(!s);
			});
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
		
		@UiField MaterialValueBox<String> tbRole;
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

		@UiHandler("tbRole")
		public void onValueChange(ValueChangeEvent<?> e) {
			roleSet.add(tbRole.getValue().toUpperCase());
			tbRole.clear();
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
				Scheduler.get().scheduleDeferred(() -> tbRole.setFocus(true));
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
		@UiField MaterialLink projectSet, projectRole;
		@UiField MaterialChip selectedAccount, selectedProject;
		@UiField Label bgAccountRoles, bgProjectRoles;
		@UiField MaterialWidget rolesContent, bxAccountRoles, bxProjectRoles;

		@UiHandler("accountSelect")
		void onAccountSelect(ClickEvent e) {
			getUiHandlers().onAccountSelect();
		}

		@UiHandler("projectCreate")
		void onProjectCreate(ClickEvent e) {
			getUiHandlers().onProjectCreate();
		}

		@UiHandler("projectSelect")
		void onProjectSelect(ClickEvent e) {
			getUiHandlers().onProjectSelect();
		}

		@UiHandler("projectSet")
		void onProjectSet(ClickEvent e) {
			getUiHandlers().onProjectEdit();
		}

		@UiHandler("projectRole")
		void onProjectRole(ClickEvent e) {
			getUiHandlers().onProjectRole();
		}

		private void render() {
			bxAccountRoles.clear();
			for(String role:accountRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.remove(l.getText());
					projectRoleSet.add(l.getText());
					render();
					getUiHandlers().grantAccountRoles(selectedAccount.getValue(), selectedProject.getValue(), accountRoleSet);
				});
				bxAccountRoles.add(chip);
			}

			bxProjectRoles.clear();
			for(String role:projectRoleSet) {
				Label chip = new Label(role);
				chip.addClickHandler(e -> {
					Label l = (Label) e.getSource();
					accountRoleSet.add(l.getText());
					projectRoleSet.remove(l.getText());
					render();
					getUiHandlers().grantAccountRoles(selectedAccount.getValue(), selectedProject.getValue(), accountRoleSet);
				});
				bxProjectRoles.add(chip);
			}
		}

		private Set<String> projectRoleSet, accountRoleSet;

		@Override
		protected void onAttach() {
			super.onAttach();

			rolesContent.setVisible(false);
			getUiHandlers().getRoles((account, project) -> {
				selectedAccount.setValue(account);
				selectedAccount.setVisible((null != account && !account.isEmpty()));
				bgAccountRoles.setText("账户["+ account + "]设定的角色");
				selectedProject.setValue(project);
				selectedProject.setVisible((null != project && !project.isEmpty()));
				bgProjectRoles.setText("项目["+ project + "]可分配的角色");
				projectSet.setEnabled((null != project && !project.isEmpty()));
				projectRole.setEnabled((null != project && !project.isEmpty()));
			}, (accountRoles, projectRoles) -> {
				accountRoleSet = accountRoles;
				projectRoleSet = projectRoles;
				
				// Remove roles from projectSet which accountSet already granted.
				for(String s:accountRoleSet)
					projectRoleSet.remove(s);

				rolesContent.setVisible(true);
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
