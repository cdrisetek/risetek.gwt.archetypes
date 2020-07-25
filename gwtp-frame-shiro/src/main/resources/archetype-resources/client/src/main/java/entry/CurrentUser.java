package ${package}.entry;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import ${package}.entry.AuthorityChangedEvent.AuthorityChangedHandler;
import ${package}.share.GetResult;
import ${package}.share.SecurityAction;
import ${package}.share.SecurityAction.OP;
import ${package}.share.realmgt.AccountDescriptionsEntity;
import ${package}.share.realmgt.AccountEntity;
import ${package}.utils.ServerExceptionHandler;

@Singleton
public class CurrentUser implements AuthorityChangedHandler {

	private AccountEntity subject;

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
	
	private void doAction(SecurityAction action, Consumer<String> success, Consumer<Throwable> fail) {
		dispatcher.execute(action, new AsyncCallback<GetResult<AccountEntity>>() {
			@Override
			public void onFailure(Throwable caught) {
				exceptionHandler.handler(caught);
				fail.accept(caught);
			}

			@Override
			public void onSuccess(GetResult<AccountEntity> result) {
				subject = result.getResults();
				success.accept("success");
			}});
	}

	public void Login(String username, String password, boolean rememberme, Consumer<Throwable> loginFailure) {
		AccountEntity sub = new AccountEntity();
		sub.setAccountDescriptions(new AccountDescriptionsEntity());
		sub.setAccountPrincipal(username);

		SecurityAction action = new SecurityAction(sub, password, rememberme, OP.LOGIN);
		doAction(action, c->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), fail->loginFailure.accept(fail));
	}
    
	public void Logout() {
		SecurityAction action = new SecurityAction(null, null, false, OP.LOGOUT);
		doAction(action, s->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), f->GWT.log("logout failed."));
	}

	public void accountSync(Consumer<String> success, Consumer<Throwable> failure) {
		SecurityAction action = new SecurityAction(null, null, false, OP.SYNC);
		doAction(action, c->success.accept("success"), fail->failure.accept(fail));
	}
	
	@Override
	public void onAuthorityChanged() {
		accountSync(c->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), fail->GWT.log("sync account failed."));
	}
	
	public AccountEntity getAccount() {
		return subject;
	}

	public boolean isLogin() {
		return null != subject;
	}
	
	public String getAccountAttribute(String key) {
		if(null == subject)
			return null;

		if("email".equals(key))
			return subject.getAccountDescriptions().getEmail();

		return null;
	}

	public void changePassword(String newPassword, Consumer<String> consumer) {
		SecurityAction action = new SecurityAction(null, newPassword, false, OP.UPDATE);
		doAction(action, consumer, fail->{GWT.log("change password failed.");});
	}

	public void changeEmail(String newEmail, Consumer<String> consumer) {
		AccountDescriptionsEntity attribute = new AccountDescriptionsEntity();
		attribute.setEmail(newEmail);
		AccountEntity newAccount = new AccountEntity();
		newAccount.setAccountDescriptions(attribute);

		SecurityAction action = new SecurityAction(newAccount, null, false, OP.UPDATE);
		doAction(action, consumer, fail->{GWT.log("change email failed.");});
	}
	
	public void newAccount(String username, String password) {
		GWT.log("new Account:" + username + " with password:" + password);
	}
	
	public boolean checkRole(String role) {
		if(null == subject)
			return false;
		return subject.getRoles().contains(role);
	}
	public void resetPassword(String email) {
		
	}
}
