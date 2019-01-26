package ${package}.login;

import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.risetek.login.LoginWidget.LoginSubmitHandle;

class MyViewImpl implements LoginView {
	private Presenter presenter;

	private final LoginWidget loginWidget;

    @Inject
    public MyViewImpl() {
    	loginWidget = new LoginWidget(new LoginSubmitHandle(){

    		@Override
    		public void onSubmit(String username, String password) {
    			presenter.Login(username, password, c->onFailed());
    		}});
    }

	@Override
	public Widget asWidget() {
		return loginWidget;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void onFailed() {
		loginWidget.setFaliedStatus();
	}
}
