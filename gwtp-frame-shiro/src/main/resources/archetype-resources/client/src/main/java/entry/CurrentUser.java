package ${package}.entry;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import ${package}.AuthToken;
import ${package}.GetResult;
import ${package}.LoginOutAction;
import ${package}.entry.AuthorityChangedEvent.AuthorityChangedHandler;
import ${package}.AuthorityInfo;

@Singleton
public class CurrentUser implements AuthorityChangedHandler {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private AuthorityInfo authority;

	@Inject
	public CurrentUser(DispatchAsync dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		eventBus.addHandler(AuthorityChangedEvent.getType(), this);
		// load authority information from server.
		onAuthorityChanged();
	}
	
	public void Login(String username, String password, boolean rememberme, Consumer<Throwable> loginFailure) {
		AuthToken token = new AuthToken();
		token.setUsername(username);
		token.setPassword(password);
		token.setRememberMe(true);
		LoginOutAction action = new LoginOutAction(token);
		dispatcher.execute(action, new AsyncCallback<GetResult<AuthorityInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				loginFailure.accept(caught);
			}

			@Override
			public void onSuccess(GetResult<AuthorityInfo> result) {
				authority = result.getResults();
				eventBus.fireEvent(UserRolesChangeEvent.INSTANCE);
			}
		});
	}
    
	public void Logout() {
		LoginOutAction action = new LoginOutAction(null);
		dispatcher.execute(action, new AsyncCallback<GetResult<AuthorityInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(GetResult<AuthorityInfo> result) {
				authority = result.getResults();
				eventBus.fireEvent(UserRolesChangeEvent.INSTANCE);
			}
		});
	}

	@Override
	public void onAuthorityChanged() {
		Login(null, null, false, null);
	}

	public AuthorityInfo getAuthorityInfo() {
		return authority;
	}

	public boolean isLogin() {
		return authority.isLogin();
	}
}
