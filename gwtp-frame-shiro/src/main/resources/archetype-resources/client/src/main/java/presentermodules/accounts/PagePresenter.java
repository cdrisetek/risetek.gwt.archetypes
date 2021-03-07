package ${package}.presentermodules.accounts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.entry.LoggedInGatekeeper;
import ${package}.place.root.RootPresenter;
import ${package}.share.accounts.AccountAction;
import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import ${package}.share.accounts.roles.RoleAction;
import ${package}.share.accounts.roles.RoleEntity;
import ${package}.share.dispatch.GetResult;
import ${package}.share.dispatch.GetResults;
import ${package}.utils.ServerExceptionHandler;

public class PagePresenter extends Presenter<PagePresenter.MyView, PagePresenter.MyProxy> implements MyUiHandlers {
	public interface MyView extends View, HasUiHandlers<MyUiHandlers> {
		void showAccountView();
		void showCreateView();
		void showEditView();

		void Message(String message);
	}

	@ProxyCodeSplit
	@NameToken(TokenNames.account)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PagePresenter> {}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private final ServerExceptionHandler exceptionHandler;
	@Inject
	public PagePresenter(final EventBus eventBus,
			             final MyView view,
			             final MyProxy proxy,
			             final DispatchAsync dispatcher,
			             final PlaceManager placeManager,
			             final ServerExceptionHandler exceptionHandler) {
		super(eventBus, view, proxy, RootPresenter.SLOT_MainContent);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.exceptionHandler = exceptionHandler;
		getView().setUiHandlers(this);
	}

	private final PlaceRequest backPlace = new PlaceRequest.Builder().nameToken(NameTokens.home).build();
	@Override
	public void onGoBackPlace() {
		placeManager.revealPlace(backPlace);
	}

	private final PlaceRequest accountSelectPlace = new PlaceRequest.Builder().nameToken(TokenNames.accounts)
                                                       .with(TokenNames.selector, getProxy().getNameToken() /* called from me */)
                                                       .build();
	@Override
	public void onAccountSelect() {
		placeManager.revealPlace(accountSelectPlace, false);
	}
	
	@Override
	public void onAccountCreate() {
		getView().showCreateView();
	}

	private String selectedAccount;
	
	@Override
	public void onAccountEdit() {
		if(null == selectedAccount)
			return;

		getView().showEditView();
	}

	@Override
	public void onReveal() {
		selectedAccount = placeManager.getCurrentPlaceRequest().getParameter(TokenNames.select, selectedAccount);
		getView().showAccountView();
	}
	
	@Override
	public void createAccount(String name, String password, Map<String, String> descriptions) {
		assert (null != name && !name.isEmpty() && null != password && !password.isEmpty()) : "Invalid createAccount";

		AccountEntity entity = new AccountEntity();
		entity.setPrincipal(name);
		entity.setDescriptions(descriptions);

		AccountAction action = new AccountAction(password, entity);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().Message("新用户成功创建");
				selectedAccount = name;
				getView().showAccountView();
			}
		});
	}

	@Override
	public void updateAccount(String name, Map<String, String> descriptions) {
		AccountEntity entity = new AccountEntity();
		entity.setPrincipal(name);
		entity.setDescriptions(descriptions);

		AccountAction action = new AccountAction(entity);
		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				getView().Message("账户信息更新");
				getView().showAccountView();
			}
		});
	}

	@Override
	public void getAccount(BiConsumer<String, Map<String, String>> account) {
		AccountEntity entity = new AccountEntity();
		entity.setPrincipal(selectedAccount);

		AccountAction action = new AccountAction(new HashSet<>(Arrays.asList(entity)), 0, 1, null);
		sequence = Math.max(sequence, action.sequence);

		dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed.");
				exceptionHandler.handler(caught);
			}

			@Override
			public void onSuccess(GetResults<AccountEntity> result) {
				if(action.sequence < sequence)
					GWT.log("[DEBUG] find action delayed");

				if(result.getResults().size() > 0) {
					AccountEntity entity = result.getResults().get(0);
					account.accept(entity.getPrincipal(),entity.getDescriptions());
				} else
					placeManager.revealDefaultPlace();
			}
		});
	}

	@Override
	public void getAccountRoles(Consumer<String> name, BiConsumer<Set<String>, Set<String>> roles) {
		if(null == selectedAccount)
			return;
		
		RoleAction action = new RoleAction(selectedAccount, null);
		sequence = Math.max(sequence, action.sequence);
		dispatcher.execute(action, new AsyncCallback<GetResult<RoleEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed: " + caught);
			}

			@Override
			public void onSuccess(GetResult<RoleEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				Set<String> projectRoles = Arrays.asList(HostProjectRBAC.values()).stream()
						                         .map(HostProjectRBAC::name).collect(Collectors.toSet());
				name.accept(selectedAccount);
				roles.accept(result.getResults().getRoleSet(), projectRoles);
			}
		});
	}
	
	@Override
	public void updateAccountRoles(Set<String> roles) {
		RoleEntity entity = new RoleEntity();
		entity.addRoleSet(roles);
		
		RoleAction action = new RoleAction(selectedAccount, null, entity);
		sequence = Math.max(sequence, action.sequence);
		dispatcher.execute(action, new AsyncCallback<GetResult<RoleEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().Message("Server State Failed: " + caught);
			}

			@Override
			public void onSuccess(GetResult<RoleEntity> result) {
				if(action.sequence < sequence)
					GWT.log("find action delayed");
				getView().Message("roles updated");
			}
		});
	}

	private long sequence = 0;
	@Override
	public void checkValidate(String value, Consumer<AccountValidate> state) {
		if(null == value || value.isEmpty())
			state.accept(AccountValidate.UNKNOWN);
		else if(value.length() > 4) {
			state.accept(AccountValidate.CHECKING);
			
			AccountEntity entity = new AccountEntity();
			entity.setPrincipal(value);

			AccountAction action = new AccountAction(new HashSet<>(Arrays.asList(entity)), 0, 1, null);
			sequence = Math.max(sequence, action.sequence);

			dispatcher.execute(action, new AsyncCallback<GetResults<AccountEntity>>() {

				@Override
				public void onFailure(Throwable caught) {
					getView().Message("Server State Failed.");
					exceptionHandler.handler(caught);
				}

				@Override
				public void onSuccess(GetResults<AccountEntity> result) {
					if(action.sequence < sequence) {
						GWT.log("find action delayed");
						return;
					}
					if(result.getResults().size() > 0)
						state.accept(AccountValidate.INVALIDATE);
					else
						state.accept(AccountValidate.VALIDATE);
				}
			});
			
		} else
			state.accept(AccountValidate.INVALIDATE);
	}
}
