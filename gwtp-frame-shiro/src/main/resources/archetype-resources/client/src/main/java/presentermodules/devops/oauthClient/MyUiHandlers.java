package ${package}.presentermodules.devops.oauthClient;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	void onGoBackPlace();
	void onOAuthClientRequest();
	void onOAuthTokenRequest(String code, String state);
}
