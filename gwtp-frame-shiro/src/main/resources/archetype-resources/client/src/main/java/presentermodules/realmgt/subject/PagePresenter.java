package ${package}.presentermodules.realmgt.subject;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.bindery.PlainMenu;
import ${package}.entry.CurrentUser;
import ${package}.presentermodules.realmgt.TokenNames;
import ${package}.root.RootPresenter;
import ${package}.share.GetResults;
import ${package}.utils.ServerExceptionHandler;
import ${package}.share.realmgt.AccountEntity;
import ${package}.share.realmgt.SubjectAction;
import ${package}.entry.LoggedInGatekeeper;

@PlainMenu(order = 1001, title = "账户管理", token = TokenNames.realmgt)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void setSubjects(List<AccountEntity> subjects);
		int getSubjectsCapacity();
		void upDatePagerStatus(int offset, boolean hasMore);
		void alert(String message);
		public void showSubjectCreatePlace();
		public void closeSubjectCreatePlace();
		public void showSubjectMaintancePlace(AccountEntity subject);
		public void closeSubjectMaintancePlace();
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.realmgt)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {
	}

	private final CurrentUser user;
	private final DispatchAsync dispatcher;
	private final ServerExceptionHandler exceptionHandler;

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, DispatchAsync dispatcher,
			CurrentUser user, ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		this.user = user;
	}

	private int subjectOffset = 0;
	private long sequence = 0;
	private long lastsequence = 0;
	private String like = null;

	private void readSubjects(int direction) {

		int size = getView().getSubjectsCapacity();
		int offset = subjectOffset;
		if (direction == -1)
			offset -= size;
		else if (direction == 1)
			offset += size;

		if (offset < 0)
			offset = 0;

		// read more than require so to determine the end of data.
		SubjectAction action = new SubjectAction(null, null, SubjectAction.OP.READ, offset, (size + 1), like, sequence++);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				if (result.getResults().size() == 0)
					return;

				if (action.sequence < lastsequence) {
					GWT.log("discard action: " + action.sequence);
					return;
				}

				lastsequence = action.sequence;

				getView().setSubjects(result.getResults());

				subjectOffset = action.offset;
				getView().upDatePagerStatus(subjectOffset, result.getResults().size() > size);
			}
		});
	}

	@Override
	public void onSubjectPlace() {
		// TODO: check role.
		if(!user.checkRole("admin")) {
			getView().alert("you have no privage to create new account");
			return;
		}
		getView().showSubjectCreatePlace();
	}
	
	@Override
	public void onSubjectCreate(Set<AccountEntity> subjects, String password) {
		// read more than require so to determine the end of data.
		SubjectAction action = new SubjectAction(subjects, password, SubjectAction.OP.CREATE, 0, 0, null, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().alert("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().alert("新用户成功创建");
				getView().closeSubjectCreatePlace();
				readSubjects(0);
			}
		});
	}

	@Override
	public void onSubjectSearch(String like) {
		if (like.length() == 0)
			this.like = null;
		else
			this.like = like;

		onSubjectTablePagerHome();
	}

	@Override
	public void onSubjectTablePagerFlush(boolean isResized, boolean forceLoad) {
		if (forceLoad)
			subjectOffset = 0;
		readSubjects(0);
	}

	@Override
	public void onSubjectTablePager(int dir) {
		readSubjects(dir);
	}

	@Override
	public void onSubjectTablePagerHome() {
		subjectOffset = 0;
		readSubjects(0);
	}

	@Override
	public void onSubjectMaintance(AccountEntity subject) {
		getView().showSubjectMaintancePlace(subject);
	}

	@Override
	public void onSubjectUpdate(Set<AccountEntity> subjects) {
		// read more than require so to determine the end of data.
		SubjectAction action = new SubjectAction(subjects, null, SubjectAction.OP.UPDATE, 0, 0, null, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().alert("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().alert("账户更新成功");
				getView().closeSubjectMaintancePlace();
				readSubjects(0);
			}
		});
	}

}
