package ${package}.entry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.AuthenticationAction;
import ${package}.share.accounts.AuthorizationAction;
import ${package}.share.accounts.AuthorizationEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.share.accounts.SubjectAction;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.dispatch.GetResult;
import ${package}.share.dispatch.GetResults;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction.OnException;
import ${package}.utils.ServerExceptionHandler;

@Singleton
public final class Subject {
	/*
	 * Subject for this session include authorization information such as roles
	 * and user information such as descriptions.
	 * Synchronize with server side, we should get user information and roles.  
	 */
	private volatile Set<String> subjectRoles;
	private volatile Map<String, String> subjectDescriptions;
	private volatile String subjectPrincipal;

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private final ServerExceptionHandler exceptionHandler;

	@Inject
	public Subject(final DispatchAsync dispatcher,
			final EventBus eventBus,
			final ServerExceptionHandler exceptionHandler) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.exceptionHandler = exceptionHandler;
	}
	
	private void doSubjectBatchAction(Consumer<Throwable> failure, Action<?>... actions) {
		dispatcher.execute(new UnsecuredSerializableBatchAction(OnException.ROLLBACK, actions),
			new AsyncCallback<GetResults<GetResult<? extends IsSerializable>>>() {
				@Override
				public void onFailure(Throwable caught) {
					resetSubject();
					if(null != failure)
						failure.accept(caught);
					else
						exceptionHandler.handler(caught);

					eventBus.fireEvent(SubjectChangeEvent.INSTANCE);
				}

				@Override
				public void onSuccess(GetResults<GetResult<?>> result) {
					List<GetResult<?>> results = result.getResults();
					resetSubject();
					results.forEach(entity -> {
						Object obj = entity.getResults();
						if(obj instanceof GetResult) {
							obj = ((GetResult<?>)obj).getResults();
							if(obj instanceof AuthorizationEntity) {
								subjectRoles = ((AuthorizationEntity)obj).getRole().getRoleSet();
							} else if(obj instanceof AccountEntity) {
								subjectDescriptions = ((AccountEntity)obj).getDescriptions();
								subjectPrincipal = ((AccountEntity)obj).getPrincipal();
							}
						}
					});
					if(null == subjectPrincipal && null != failure)
						failure.accept(new Throwable("login failed"));
					GWT.log("subject changed");
					eventBus.fireEvent(SubjectChangeEvent.INSTANCE);
				};
			});	
	}
	
	/*
	 * Login function send Authentication data to server, then get authorization data and user data from server.
	 */
	public void Login(String username, String password, boolean remember, String project, Consumer<Throwable> failure) {
		resetSubject();
		AuthenticationAction authenAction = 
				new AuthenticationAction(Arrays.asList(username), password, remember, project);

		doSubjectBatchAction(failure,
				// just for development
				new AccountAction("key" /* just make handler happy */),
				authenAction, 
				new AuthorizationAction(), /* To read subject roles */
				new SubjectAction() /* To read subject descriptions */);
	}
    
	public void Login() {
		resetSubject();
        String param = Location.createUrlBuilder().setPath("/oauth/login").buildString();
        Location.assign(param);
	}
	
	public void Logout() {
		resetSubject();
        String param = Location.createUrlBuilder().setPath("/oauth/logout").buildString();
        Location.assign(param);
	}

	public void accountSync() {
		resetSubject();
		GWT.log("account sync");
		doSubjectBatchAction(null,
				new AuthorizationAction(),  /* To read subject roles */
				new SubjectAction() /* To read subject descriptions */);
	}
	
	private final void resetSubject() {
		subjectPrincipal = null;
		subjectRoles = null;
		subjectDescriptions = null;
	}
	/*
	 * TODO: When Server side throw UnAuthorization Exception, this function be called to synchronize subject.
	 */
	public boolean isLogin() {
		return (null != subjectPrincipal) && (null != subjectRoles);
	}
	
	public String getAccountAttribute(String key) {
		return (null == subjectDescriptions) ? null : subjectDescriptions.get(key);
	}

	/*
	 * Change password only work for this subject associated user.
	 */
	public void changePassword(String newPassword, Consumer<String> consumer) {
		SubjectAction action = new SubjectAction(newPassword);
		dispatcher.execute(action, new AsyncCallback<GetResult<AccountEntity>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Notes: When server side change password, it logout subject,
				// and then throws ActionUnauthenticatedException.
				// so client run to here and handler ActionUnauthenticatedException,
				// This exception revealUnauthorizedPlace, it possible a Login page. 
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResult<AccountEntity> result) {
				consumer.accept("success");
			}});
	}

	public void changeDescription(String key, String value, Consumer<String> consumer) {
		AccountEntity userEntity = new AccountEntity();
		userEntity.setDescription(key, value);

		SubjectAction action = new SubjectAction(userEntity);
		dispatcher.execute(action, new AsyncCallback<GetResult<AccountEntity>>() {
			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResult<AccountEntity> result) {
				// TODO: reload subjectDescriptions
				GWT.log("update " + key + " successed");
				consumer.accept("success");
			}});
	}
	
	public void changeEmail(String newEmail, Consumer<String> consumer) {
		changeDescription(EnumAccount.EMAIL.name(), newEmail, consumer);
	}
	
	public void newAccount(String username, String password) {
		GWT.log("new Account:" + username + " with password:" + password);
	}
	
	public Set<String> getRoles() {
		return subjectRoles;
	}

	public boolean checkRole(String role) {
		if(null == subjectRoles)
			return false;
		return subjectRoles.contains(role);
	}
	
	public boolean checkRole(HostProjectRBAC role) {
		if(null == subjectRoles)
			return false;
		return subjectRoles.contains(role.name());
	}

	public String getSubjectPrincipal() {
		return subjectPrincipal;
	}
	public void resetPassword(String email) {
		
	}
}
