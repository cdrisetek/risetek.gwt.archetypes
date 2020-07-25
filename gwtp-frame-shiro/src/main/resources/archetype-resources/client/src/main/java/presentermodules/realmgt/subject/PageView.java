package ${package}.presentermodules.realmgt.subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.MaterialValueBox;
import ${package}.share.realmgt.AccountDescriptionsEntity;
import ${package}.share.realmgt.AccountEntity;
import ${package}.utils.KeyBoardCode;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	@UiField
	ResizeLayoutPanel resizePanel;

	@UiField
	MaterialSearch searchinput;

	@UiField
	MaterialButton btnBack, btnFirstPage, btnNext, btnCommit, btnmCommit, btnmEnable, btnmDisable;

	@UiField
	SubjectTable subjectsDisplay;

	@UiField
	MaterialDialog subjectCreateDialog, subjectMaintanceDialog;

	@UiField
	MaterialValueBox<String> name, email, password, telphone, notes, mname, memail, mtelphone, mnotes;

	interface Binder extends UiBinder<HTMLPanel, PageView> {
	}

	private final ListDataProvider<AccountEntity> dataProvider = new ListDataProvider<>();

	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		dataProvider.addDataDisplay(subjectsDisplay);

		searchinput.addKeyUpHandler(c -> {
			getUiHandlers().onSubjectSearch(searchinput.getText());
		});

		searchinput.addCloseHandler(event -> {
			searchinput.setValue(null);
			getUiHandlers().onSubjectSearch(searchinput.getText());
		});
		
		subjectsDisplay.addSubjectSelectedHandler(c->getUiHandlers().onSubjectMaintance(c));
	}
	
	@UiHandler("resizePanel")
	void onResize(ResizeEvent e) {
		subjectsDisplay.setPageSize((e.getHeight() - subjectsDisplay.getHeaderHeight()) / rowHeight);
		getUiHandlers().onSubjectTablePagerFlush(true, false);
	}

	@UiHandler("btnBack")
	void onBackClick(ClickEvent e) {
		getUiHandlers().onSubjectTablePager(-1);
	}

	@UiHandler("btnNext")
	void onNextClick(ClickEvent e) {
		getUiHandlers().onSubjectTablePager(1);
	}

	@UiHandler("btnFirstPage")
	void onHomeClick(ClickEvent e) {
		getUiHandlers().onSubjectTablePagerHome();
	}

	@UiHandler("btnRefresh")
	void onRefreshClick(ClickEvent e) {
		getUiHandlers().onSubjectTablePagerFlush(false, false);
	}

	@UiHandler("btnCancel")
	void onCancelClick(ClickEvent e) {
		closeSubjectCreatePlace();
	}

	@UiHandler("btnCommit")
	void onCommitCreatorPlaceClick(ClickEvent e) {
		AccountEntity subject = new AccountEntity();
		subject.setEnable(true);
		AccountDescriptionsEntity principal = new AccountDescriptionsEntity();
		subject.setAccountPrincipal(name.getValue());
		principal.setEmail(email.getValue());
		principal.setTelphone(telphone.getValue());
		principal.setNotes(notes.getValue());
		
		subject.setAccountDescriptions(principal);

		if (subject.getAccountPrincipal() == null || subject.getAccountPrincipal().isEmpty()) {
			alert("name empty");
			return;
		}

		Set<AccountEntity> subjects = new HashSet<>();
		subjects.add(subject);
		
		getUiHandlers().onSubjectCreate(subjects, password.getValue());
	}

	@UiHandler("btnCreate")
	void onAddNewClick(ClickEvent e) {
		getUiHandlers().onSubjectPlace();
	}

	@Override
	public void showSubjectCreatePlace() {
		name.clear();
		password.clear();
		email.clear();
		telphone.clear();
		notes.clear();
		
		name.setFocus(true);
		subjectCreateDialog.open();
	}

	@UiHandler("btnmCancel")
	void onCancelMaintancePlaceClick(ClickEvent e) {
		closeSubjectMaintancePlace();
	}
	
	private void maintanceAccountEnable(boolean enable) {
		AccountEntity subject = new AccountEntity();
		subject.setAccountPrincipal(mname.getValue());
		subject.setEnable(enable);

		Set<AccountEntity> subjects = new HashSet<>();
		subjects.add(subject);

		getUiHandlers().onSubjectUpdate(subjects);
	}
	@UiHandler("btnmDisable")
	void onMaintancePlaceAccountDisableClick(ClickEvent e) {
		maintanceAccountEnable(false);
	}
	@UiHandler("btnmEnable")
	void onMaintancePlaceAccountEnableClick(ClickEvent e) {
		maintanceAccountEnable(true);
	}
	
	@UiHandler("btnmCommit")
	void onCommitMaintancePlaceClick(ClickEvent e) {
		AccountEntity subject = new AccountEntity();
		subject.setAccountPrincipal(mname.getValue());
		AccountDescriptionsEntity principal = new AccountDescriptionsEntity();
		
		principal.setEmail(memail.getValue());
		principal.setTelphone(mtelphone.getValue());
		principal.setNotes(mnotes.getValue());
		
		subject.setAccountDescriptions(principal);

		Set<AccountEntity> subjects = new HashSet<>();
		subjects.add(subject);

		getUiHandlers().onSubjectUpdate(subjects);
	}

	@Override
	public void showSubjectMaintancePlace(AccountEntity subject) {
		mname.setValue(subject.getAccountPrincipal());
		memail.setValue(subject.getAccountDescriptions().getEmail());
		mtelphone.setValue(subject.getAccountDescriptions().getTelphone());
		mnotes.setValue(subject.getAccountDescriptions().getNotes());
		if(subject.isEnable()) {
			btnmEnable.setDisplay(Display.NONE);
			btnmDisable.setDisplay(Display.BLOCK);
		} else {
			btnmEnable.setDisplay(Display.BLOCK);
			btnmDisable.setDisplay(Display.NONE);
		}
		subjectMaintanceDialog.open();
	}
	
	@Override
	public void alert(String message) {
		MaterialToast.fireToast(message);
	}

	private HandlerRegistration nativePreviewHandlerReg;

	@Override
	protected void onDetach() {
		super.onDetach();
		nativePreviewHandlerReg.removeHandler();
		nativePreviewHandlerReg = null;
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		nativePreviewHandlerReg = Event.addNativePreviewHandler(event -> {
			int eventType = event.getTypeInt();
			if (eventType == Event.ONKEYDOWN) {
				int keyCode = event.getNativeEvent().getKeyCode();
				// prevent this event when popup panel show
				if (event.isFirstHandler()) {
					if (KeyBoardCode.DOWN == keyCode || KeyBoardCode.PAGEDOWN == keyCode)
						getUiHandlers().onSubjectTablePager(1);
					else if (KeyBoardCode.UP == keyCode || KeyBoardCode.PAGEUP == keyCode)
						getUiHandlers().onSubjectTablePager(-1);
					else if(KeyBoardCode.ENTER == keyCode) {
						if(subjectMaintanceDialog.isOpen()) {
						    NativeEvent evt = Document.get().createClickEvent(1, 0, 0, 0, 0, false,
							        false, false, false);
						    btnmCommit.getElement().dispatchEvent(evt);
						}
						if(subjectCreateDialog.isOpen()) {
						    NativeEvent evt = Document.get().createClickEvent(1, 0, 0, 0, 0, false,
						        false, false, false);
						    btnCommit.getElement().dispatchEvent(evt);
						}
					}
				}
			} else if (eventType == Event.ONMOUSEWHEEL) {
				if (event.isFirstHandler()) {
					int mouseY = event.getNativeEvent().getMouseWheelVelocityY();
					if (mouseY < 0)
						getUiHandlers().onSubjectTablePager(-1);
					else
						getUiHandlers().onSubjectTablePager(1);
				}
			}
		});
	}

	@Override
	public void setSubjects(List<AccountEntity> subjects) {
		// fill the table space. this is simple way.
		for (int i = subjects.size(); i <= subjectsDisplay.getPageSize(); i++)
			subjects.add(null);

		dataProvider.setList(subjects);

		// Set the total row count. This isn't strictly necessary, but it affects
		// paging calculations, so its good habit to keep the row count up to date.
		subjectsDisplay.setRowCount(subjectsDisplay.getPageSize(), true);
	}

	private int rowHeight = 100; // Unit.PX

	@Override
	public int getSubjectsCapacity() {
		return subjectsDisplay.getPageSize();
	}

	@Override
	public void upDatePagerStatus(int offset, boolean hasMore) {
		btnFirstPage.setEnabled(offset != 0);
		btnBack.setEnabled(offset != 0);
		btnNext.setEnabled(hasMore);
	}

	@Override
	public void closeSubjectCreatePlace() {
		subjectCreateDialog.close();
	}

	@Override
	public void closeSubjectMaintancePlace() {
		subjectMaintanceDialog.close();
	}

}
