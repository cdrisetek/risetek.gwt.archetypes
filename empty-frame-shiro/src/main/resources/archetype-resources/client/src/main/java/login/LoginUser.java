package ${package}.login;

import java.util.function.Consumer;

import javax.inject.Singleton;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import ${package}.FreeFactory;
import ${package}.login.AuthorityChangedEvent.AuthorityChangedHandler;

@Singleton
public class LoginUser implements AuthorityChangedHandler {
	private EventBus eventBus;
	private FreeFactory freefactory;

	private AuthorityResponseProxy authority = null;
	
	public String getUserName() {
		if(null == authority)
			return null;
		return authority.getUsrname();
	}
	
	public boolean isLogin() {
		if(null == authority)
			return false;
		return authority.isLogin();
	}
	
	public boolean hasRole(String role) {
		if(null == authority)
			return false;
		return authority.getRoles().contains(role);
	}

	public LoginUser initialize(FreeFactory freefactory, EventBus eventBus) {
		this.freefactory = freefactory;
		this.eventBus = eventBus;
		this.eventBus.addHandler(AuthorityChangedEvent.getType(), this);
		return this;
	}
	
    private void forceSync() {
    	Login(null,null,null);
    }

	public void Login(String username, String password, Consumer<ServerFailure> loginFailure) {
		freefactory.loginContext().login(username, password).fire(
				new Receiver<AuthorityResponseProxy>() {
					public void onFailure(ServerFailure failure) {
						if(null != loginFailure)
							loginFailure.accept(failure);
					}

					@Override
					public void onSuccess(AuthorityResponseProxy response) {
						authority = response;
						eventBus.fireEvent(UserRolesChangeEvent.INSTANCE);
					}
				});
	}
    
	public void Logout() {
		freefactory.loginContext().logout().fire(
				new Receiver<Void>() {
					public void onFailure(ServerFailure failure) {
					}

					@Override
					public void onSuccess(Void response) {
						eventBus.fireEvent(new AuthorityChangedEvent());
					}
				});
	}
    
	@Override
	public void onAuthorityChanged() {
		forceSync();
	}
}
