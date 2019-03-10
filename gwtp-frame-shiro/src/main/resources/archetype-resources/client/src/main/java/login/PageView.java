package ${package}.login;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.login.LoginWidget.LoginSubmitHandle;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {

	private final LoginWidget loginWidget = new LoginWidget(true, new LoginSubmitHandle(){

		@Override
		public void onSubmit(String username, String password, boolean rememberme) {
			getUiHandlers().Login(username, password, rememberme);
		}});
    
	private final StyleLogin.Style style = StyleLogin.resources.style();
	
    @Inject
    public PageView() {
    	style.ensureInjected();
    	SimplePanel container = new SimplePanel();
    	container.setStyleName(style.loginWidgetContainer());
    	container.add(loginWidget);
        initWidget(container);
    }

	@Override
	public void setStatus(String status) {
		loginWidget.setStatus(status);
	}
}
