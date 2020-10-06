package ${package}.entry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.Action;
import ${package}.entry.AuthorityChangedEvent.AuthorityChangedHandler;
import ${package}.share.GetResult;
import ${package}.utils.ServerExceptionHandler;
import ${package}.share.GetResults;
import ${package}.share.UnsecuredSerializableBatchAction;
import ${package}.share.UnsecuredSerializableBatchAction.OnException;

import ${package}.share.auth.AuthenticationAction;
import ${package}.share.auth.AuthorizationAction;
import ${package}.share.auth.RoleEntity;
import ${package}.share.auth.SubjectAction;
import ${package}.share.auth.UserEntity;
import ${package}.share.users.EnumUserDescription;

@Singleton
public final class CurrentUser implements AuthorityChangedHandler {
	/*
	 * Subject for this session include authorization information such as roles
	 * and user information such as descriptions.
	 * Synchronize with server side, we should get user information and roles.  
	 */
	private volatile Set<String> subjectRoles;
	private volatile Map<String, String> subjectDescriptions;

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private final ServerExceptionHandler exceptionHandler;

	@Inject
	public CurrentUser(DispatchAsync dispatcher, EventBus eventBus, ServerExceptionHandler exceptionHandler) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.exceptionHandler = exceptionHandler;
		eventBus.addHandler(AuthorityChangedEvent.getType(), this);
	}
	
	private void doSubjectBatchAction(Consumer<Throwable> failure, Action<?>... actions) {
		dispatcher.execute(new UnsecuredSerializableBatchAction(OnException.ROLLBACK, actions),
			new AsyncCallback<GetResults<GetResult<? extends IsSerializable>>>() {
				@Override
				public void onFailure(Throwable caught) {
					if(null != failure)
						failure.accept(caught);
					else
						exceptionHandler.handler(caught);

					eventBus.fireEvent(UserRolesChangeEvent.INSTANCE);
				}

				@Override
				public void onSuccess(GetResults<GetResult<?>> result) {
					List<GetResult<?>> results = result.getResults();
					results.forEach(entity -> {
						Object obj = entity.getResults();
						if(obj instanceof GetResult) {
							obj = ((GetResult<?>)obj).getResults();
							if(obj instanceof RoleEntity)
								subjectRoles = ((RoleEntity)obj).getRole();
							else if(obj instanceof UserEntity)
								subjectDescriptions = ((UserEntity)obj).getDescriptions();
						}
					});

					eventBus.fireEvent(UserRolesChangeEvent.INSTANCE);
				};
			});	
	}
	
	/*
	 * Login function send Authentication data to server, then get authorization data and user data from server.
	 */
	public void Login(String username, String password, boolean remember, String project, Consumer<Throwable> failure) {
		subjectRoles = null;
		subjectDescriptions = null;

		AuthenticationAction authenAction = 
				new AuthenticationAction(Arrays.asList(username), password, remember, project);

		AuthorizationAction getMyAuthorizationAction = new AuthorizationAction();
		SubjectAction getMyUserAction = new SubjectAction();
		
		doSubjectBatchAction(failure, authenAction, getMyAuthorizationAction, getMyUserAction);
	}
    
	public void Logout() {
		subjectRoles = null;
		subjectDescriptions = null;

		AuthenticationAction action = new AuthenticationAction();
		doSubjectBatchAction(null, action);
	}

	public void accountSync(Consumer<String> success, Consumer<Throwable> failure) {
		AuthorizationAction getMyAuthorizationAction = new AuthorizationAction();
		SubjectAction getMyUserEntityAction = new SubjectAction();

		subjectRoles = null;
		subjectDescriptions = null;
		
		doSubjectBatchAction(failure, getMyAuthorizationAction, getMyUserEntityAction);
	}
	
	/*
	 * When Server side throw UnAuthorization Exception, this function be called to synchronize subject.
	 */
	@Override
	public void onAuthorityChanged() {
		accountSync(c->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), fail->GWT.log("Fail to synchronize subject."));
	}

	public boolean isLogin() {
		return null != subjectRoles;
	}
	
	public String getAccountAttribute(String key) {
		return (null == subjectDescriptions) ? null : subjectDescriptions.get(key);
	}

	/*
	 * Change password only work for this subject associated user.
	 */
	public void changePassword(String newPassword, Consumer<String> consumer) {
		SubjectAction action = new SubjectAction(newPassword);
		dispatcher.execute(action, new AsyncCallback<GetResult<UserEntity>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Notes: When server side change password, it logout subject,
				// and then throws ActionUnauthenticatedException.
				// so client run to here and handler ActionUnauthenticatedException,
				// This exception revealUnauthorizedPlace, it possible a Login page. 
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResult<UserEntity> result) {
				consumer.accept("success");
			}});
	}

	public void changeDescription(String key, String value, Consumer<String> consumer) {
		UserEntity userEntity = new UserEntity();
		userEntity.getDescriptions().put(key, value);

		SubjectAction action = new SubjectAction(userEntity);
		dispatcher.execute(action, new AsyncCallback<GetResult<UserEntity>>() {
			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResult<UserEntity> result) {
				// TODO: reload subjectDescriptions
				GWT.log("update " + key + " successed");
			}});
	}
	
	public void changeEmail(String newEmail, Consumer<String> consumer) {
		changeDescription(EnumUserDescription.EMAIL.name(), newEmail, consumer);
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
	public void resetPassword(String email) {
		
	}
}
