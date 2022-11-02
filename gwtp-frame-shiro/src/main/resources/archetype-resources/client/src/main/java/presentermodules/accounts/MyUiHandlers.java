package ${package}.presentermodules.accounts;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	enum AccountValidate {UNKNOWN, CHECKING, VALIDATE, INVALIDATE}
	void onGoBackPlace();
	void onAccountSelect();
	void onAccountCreate();
	void onAccountEdit();

	void createAccount(String name, String password, Map<String, String> descriptions);
	void updateAccount(String name, Map<String, String> descriptions);
	void updateAccountRoles(Set<String> roles);
	
	void getAccountRoles(Consumer<String> name, BiConsumer<Set<String>, Set<String>> roles);
	void getAccount(BiConsumer<String, Map<String, String>> account);

	void checkValidate(String value, Consumer<AccountValidate> state);
}
