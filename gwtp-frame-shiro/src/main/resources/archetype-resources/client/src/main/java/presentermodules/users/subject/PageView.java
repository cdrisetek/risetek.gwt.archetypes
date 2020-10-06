package ${package}.presentermodules.users.subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

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
import ${package}.share.auth.UserEntity;
import ${package}.share.users.EnumUserDescription;
import ${package}.share.users.UserStateEntity;
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

	private final ListDataProvider<UserEntity> dataProvider = new ListDataProvider<>();

	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		dataProvider.addDataDisplay(subjectsDisplay);

		searchinput.addKeyUpHandler(c -> {
			getUiHandlers().onUserSearch(searchinput.getText());
		});

		searchinput.addCloseHandler(event -> {
			searchinput.setValue(null);
			getUiHandlers().onUserSearch(searchinput.getText());
		});
		
		subjectsDisplay.addSubjectSelectedHandler(c->getUiHandlers().onUserMaintancePlace(c));
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

	private static void setUserEnable(UserEntity entity, boolean enable) {
		UserStateEntity state = entity.getState();
		if(null == state) {
			state = new UserStateEntity();
			entity.setState(state);
		}
		entity.getState().setEnable(enable);
	}

	private static void setUserDescription(UserEntity entity, String key, String value) {
		Map<String, String> descriptions = entity.getDescriptions();
		if(null == descriptions) {
			descriptions = new HashMap<>();
			entity.setDescriptions(descriptions);
		}
		entity.getDescriptions().put(key, value);
	}

	@UiHandler("btnCommit")
	void onCommitCreatorPlaceClick(ClickEvent e) {
		UserEntity subject = new UserEntity();
		setUserEnable(subject, true);
		setUserDescription(subject, EnumUserDescription.PRINCIPAL.name(), name.getValue());
		setUserDescription(subject, EnumUserDescription.EMAIL.name(), email.getValue());
		setUserDescription(subject, EnumUserDescription.TELPHONE.name(), telphone.getValue());
		setUserDescription(subject, EnumUserDescription.NOTES.name(), notes.getValue());

		Set<UserEntity> subjects = new HashSet<>();
		subjects.add(subject);
		
		getUiHandlers().onUserCreate(subjects, password.getValue());
	}

	@UiHandler("btnCreate")
	void onAddNewClick(ClickEvent e) {
		getUiHandlers().onUserCreatePlace();
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
		UserEntity subject = new UserEntity();
		setUserDescription(subject, EnumUserDescription.PRINCIPAL.name(), mname.getValue());
		setUserEnable(subject, enable);

		Set<UserEntity> subjects = new HashSet<>();
		subjects.add(subject);

		getUiHandlers().onUserUpdate(subjects);
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
		UserEntity subject = new UserEntity();
		setUserEnable(subject, true);
		setUserDescription(subject, EnumUserDescription.PRINCIPAL.name(), mname.getValue());
		setUserDescription(subject, EnumUserDescription.EMAIL.name(), memail.getValue());
		setUserDescription(subject, EnumUserDescription.TELPHONE.name(), mtelphone.getValue());
		setUserDescription(subject, EnumUserDescription.NOTES.name(), mnotes.getValue());

		Set<UserEntity> subjects = new HashSet<>();
		subjects.add(subject);

		getUiHandlers().onUserUpdate(subjects);
	}

	@Override
	public void showSubjectMaintancePlace(UserEntity subject) {
		mname.setValue(subject.getDescriptions().get(EnumUserDescription.PRINCIPAL.name()));
		memail.setValue(subject.getDescriptions().get(EnumUserDescription.EMAIL.name()));
		mtelphone.setValue(subject.getDescriptions().get(EnumUserDescription.TELPHONE.name()));
		mnotes.setValue(subject.getDescriptions().get(EnumUserDescription.NOTES.name()));
		if(subject.getState().isEnable()) {
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
	public void setSubjects(List<UserEntity> subjects) {
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
