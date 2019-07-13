package ${package}.entry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import ${package}.GetResult;
import ${package}.SecurityAction;
import ${package}.SecurityAction.OP;
import ${package}.SecurityInfo;
import ${package}.entry.AuthorityChangedEvent.AuthorityChangedHandler;

@Singleton
public class CurrentUser implements AuthorityChangedHandler {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private SecurityInfo authority;

	@Inject
	public CurrentUser(DispatchAsync dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		eventBus.addHandler(AuthorityChangedEvent.getType(), this);
	}
	
	private void doAction(SecurityAction action, Consumer<String> success, Consumer<Throwable> fail) {
		GWT.log("do security action");
		dispatcher.execute(action, new AsyncCallback<GetResult<SecurityInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
				fail.accept(caught);
			}

			@Override
			public void onSuccess(GetResult<SecurityInfo> result) {
				authority = result.getResults();
				success.accept("success");
			}});
	}

	public void Login(String username, String password, boolean rememberme, Consumer<Throwable> loginFailure) {
		SecurityInfo securityInfo = new SecurityInfo();
		Map<String, String> attribute = new HashMap<>();
		attribute.put("username", username);
		attribute.put("password", password);
		if(rememberme)
			attribute.put("rememberme", "true");
		
		securityInfo.setAttributes(attribute);
		SecurityAction action = new SecurityAction(securityInfo, OP.LOGIN);
		doAction(action, c->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), fail->loginFailure.accept(fail));
	}
    
	public void Logout() {
		SecurityInfo securityInfo = new SecurityInfo();
		Map<String, String> attribute = new HashMap<>();
		securityInfo.setAttributes(attribute);
		SecurityAction action = new SecurityAction(securityInfo, OP.LOGOUT);
		doAction(action, s->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), f->GWT.log("logout failed."));
	}

	public void sync(Consumer<String> success, Consumer<Throwable> failure) {
		SecurityInfo securityInfo = new SecurityInfo();
		Map<String, String> attribute = new HashMap<>();
		securityInfo.setAttributes(attribute);
		SecurityAction action = new SecurityAction(securityInfo, OP.SYNC);
		doAction(action, c->success.accept("success"), fail->failure.accept(fail));
	}
	
	@Override
	public void onAuthorityChanged() {
		sync(c->eventBus.fireEvent(UserRolesChangeEvent.INSTANCE), fail->GWT.log("sync account failed."));
	}

	public SecurityInfo getAuthorityInfo() {
		return authority;
	}

	public boolean isLogin() {
		return null != authority && authority.isLogin();
	}
	
	public String getAttribute(String key) {
		if(null == authority)
			return null;

		Map<String, String> attribute = authority.getAttributes();
		if(null == attribute)
			return null;
		
		return attribute.get(key);
	}

	private void changeSecurity(String key, String value, Consumer<String> consumer) {
		SecurityInfo securityInfo = new SecurityInfo();
		Map<String, String> attribute = new HashMap<>();
		attribute.put(key, value);
		
		securityInfo.setAttributes(attribute);
		SecurityAction action = new SecurityAction(securityInfo, OP.UPDATE);
		doAction(action, consumer, fail->{GWT.log("change attribute failed.");});
	}

	public void changePassword(String newPassword, Consumer<String> consumer) {
		changeSecurity("password", newPassword, consumer);
	}

	public void changeEmail(String newEmail, Consumer<String> consumer) {
		changeSecurity("email", newEmail, consumer);
	}
	
	public void newAccount(String username, String password) {
		GWT.log("new Account:" + username + " with password:" + password);
	}
	
	public void resetPassword(String email) {
		
	}
}
