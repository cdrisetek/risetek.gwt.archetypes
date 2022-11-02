package ${package}.presentermodules.security;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	void onGoBackPlace();

	void showPasswordView();
	void showEmailView();

	void updateEmail(String value);
	void updatePassword(String value);

	String getSecurityInformation(String key);
	
	void update(String name);
}
