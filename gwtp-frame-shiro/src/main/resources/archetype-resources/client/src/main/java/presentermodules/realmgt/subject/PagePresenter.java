package ${package}.presentermodules.realmgt.subject;

import java.util.HashSet;
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
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import ${package}.GetResults;
import ${package}.presentermodules.realmgt.TokenNames;
import ${package}.realmgt.PrincipalEntity;
import ${package}.realmgt.SubjectAction;
import ${package}.realmgt.SubjectEntity;
import ${package}.root.RootPresenter;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void setSubjects(List<SubjectEntity> subjects);

		int getSubjectsCapacity();

		void upDatePagerStatus(int offset, boolean hasMore);

		void alert(String message);

		public String getName();

		public String getEmail();

		public String getTelphone();

		public void reset();

		void dialogClose();
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.realmgt)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<PagePresenter> {
	}

	private final DispatchAsync dispatcher;

	@Inject
	public PagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, DispatchAsync dispatcher,
			PlaceManager placeManager) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
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
		SubjectAction action = new SubjectAction(null, SubjectAction.OP.READ, offset, (size + 1), like, sequence++);
		dispatcher.execute(action, new AsyncCallback<GetResults<SubjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server State Failed.");
			}

			@Override
			public void onSuccess(GetResults<SubjectEntity> result) {
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

	private void newSubjects(Set<SubjectEntity> subjects) {

		// read more than require so to determine the end of data.
		SubjectAction action = new SubjectAction(subjects, SubjectAction.OP.CREATE, 0, 0, null, 0);
		dispatcher.execute(action, new AsyncCallback<GetResults<SubjectEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Server State Failed.");
				getView().alert("Server State Failed.");
			}

			@Override
			public void onSuccess(GetResults<SubjectEntity> result) {
				getView().alert("新用户成功创建");
				getView().dialogClose();
				readSubjects(0);
			}
		});
	}

	@Override
	public void onCreateSubject() {
		SubjectEntity subject = new SubjectEntity();
		PrincipalEntity principal = new PrincipalEntity();
		subject.setPrincipal(principal);

		principal.setName(getView().getName());
		principal.setEmail(getView().getEmail());
		principal.setTelphone(getView().getTelphone());

		if (principal.getName().isEmpty()) {
			getView().alert("name empty");
			return;
		}

		Set<SubjectEntity> subjects = new HashSet<>();
		subjects.add(subject);
		newSubjects(subjects);
	}

	@Override
	public void onSearch(String like) {
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
}
