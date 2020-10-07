package ${package}.presentermodules.users.subject;

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
import ${package}.entry.Subject;
import ${package}.presentermodules.users.TokenNames;
import ${package}.root.RootPresenter;
import ${package}.share.GetResults;
import ${package}.utils.ServerExceptionHandler;
import ${package}.share.auth.EnumRBAC;
import ${package}.share.auth.UserEntity;
import ${package}.share.users.UserAction;
import ${package}.entry.LoggedInGatekeeper;

@PlainMenu(order = 1001, title = "用户管理", token = TokenNames.realmgt)
public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void setSubjects(List<UserEntity> subjects);
		int getSubjectsCapacity();
		void upDatePagerStatus(int offset, boolean hasMore);
		void alert(String message);
		public void showSubjectCreatePlace();
		public void closeSubjectCreatePlace();
		public void showSubjectMaintancePlace(UserEntity subject);
		public void closeSubjectMaintancePlace();
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.realmgt)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {
	}

	private final Subject subject;
	private final DispatchAsync dispatcher;
	private final ServerExceptionHandler exceptionHandler;

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, DispatchAsync dispatcher,
			Subject subject, ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		this.subject = subject;
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
		UserAction action = new UserAction(offset, (size + 1), like, sequence++);
		dispatcher.execute(action, new AsyncCallback<GetResults<UserEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<UserEntity> result) {
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
	public void onUserCreate(Set<UserEntity> subjects, String password) {
		// read more than require so to determine the end of data.
		UserAction action = new UserAction(subjects, password);
		dispatcher.execute(action, new AsyncCallback<GetResults<UserEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().alert("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<UserEntity> result) {
				getView().alert("新用户成功创建");
				getView().closeSubjectCreatePlace();
				readSubjects(0);
			}
		});
	}

	@Override
	public void onUserSearch(String like) {
		if (like.isEmpty())
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
	public void onUserCreatePlace() {
		// TODO: check role.
		if(!subject.checkRole(EnumRBAC.ADMIN)) {
			getView().alert("you have no privage to create new account");
			return;
		}
		getView().showSubjectCreatePlace();
	}

	@Override
	public void onUserMaintancePlace(UserEntity subject) {
		getView().showSubjectMaintancePlace(subject);
	}

	@Override
	public void onUserUpdate(Set<UserEntity> subjects) {
		// read more than require so to determine the end of data.
		UserAction action = new UserAction(subjects);
		dispatcher.execute(action, new AsyncCallback<GetResults<UserEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().alert("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<UserEntity> result) {
				getView().alert("账户更新成功");
				getView().closeSubjectMaintancePlace();
				readSubjects(0);
			}
		});
	}

}
