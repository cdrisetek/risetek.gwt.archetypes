package ${package}.presentermodules.realmgt.subject;

import java.util.List;

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
import ${package}.share.realmgt.SubjectEntity;
import ${package}.utils.KeyBoardCode;

import gwt.material.design.addins.client.inputmask.AbstractInputMask;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	@UiField
	ResizeLayoutPanel resizePanel;

	@UiField
	MaterialSearch searchinput;

	@UiField
	MaterialButton btnBack, btnFirstPage, btnNext;

	@UiField
	SubjectTable table;

	@UiField
	MaterialDialog dialog;

	@UiField
	MaterialTextBox name, email;
	@UiField
	AbstractInputMask<String> telphone;

	interface Binder extends UiBinder<HTMLPanel, PageView> {
	}

	private final ListDataProvider<SubjectEntity> dataProvider = new ListDataProvider<>();

	@Inject
	public PageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		dataProvider.addDataDisplay(table);

		searchinput.addKeyUpHandler(c -> {
			getUiHandlers().onSearch(searchinput.getText());
		});

		searchinput.addCloseHandler(event -> {
			searchinput.setValue(null);
			getUiHandlers().onSearch(searchinput.getText());
		});
		
		dialog.addCloseHandler(closeEvent->reset());
	}
	
	@UiHandler("resizePanel")
	void onResize(ResizeEvent e) {
		table.setPageSize((e.getHeight() - table.getHeaderHeight()) / rowHeight);
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
		dialogClose();
	}

	@UiHandler("btnCommit")
	void onCommitClick(ClickEvent e) {
		getUiHandlers().onCreateSubject();
	}

	@UiHandler("btnCreate")
	void onAddNewClick(ClickEvent e) {
		dialog.open();
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
	public void setSubjects(List<SubjectEntity> subjects) {
		// fill the table space. this is simple way.
		for (int i = subjects.size(); i <= table.getPageSize(); i++)
			subjects.add(null);

		dataProvider.setList(subjects);

		// Set the total row count. This isn't strictly necessary, but it affects
		// paging calculations, so its good habit to keep the row count up to date.
		table.setRowCount(table.getPageSize(), true);
	}

	private int rowHeight = 100; // Unit.PX

	@Override
	public int getSubjectsCapacity() {
		return table.getPageSize();
	}

	@Override
	public void upDatePagerStatus(int offset, boolean hasMore) {
		btnFirstPage.setEnabled(offset != 0);
		btnBack.setEnabled(offset != 0);
		btnNext.setEnabled(hasMore);
	}

	@Override
	public void dialogClose() {
		dialog.close();
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getEmail() {
		return email.getValue();
	}

	@Override
	public String getTelphone() {
		return telphone.getValue();
	}

	@Override
	public void reset() {
		name.clear();
		email.clear();
		telphone.clear();
	}

}
