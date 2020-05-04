package ${package}.presentermodules.platformMenu;

import javax.inject.Inject;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import ${package}.NameTokens;
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.entry.UserRolesChangeEvent.UserRolesChangeHandler;
import ${package}.utils.Icons;

@Singleton
public class SimpleLoginMenu extends AbstractPlatformBarMenu implements UserRolesChangeHandler {

	private final CurrentUser user;
	
	@Inject
	public SimpleLoginMenu(Binder binder, CurrentUser user, EventBus eventBus) {
		super(binder);
		this.user = user;
		eventBus.addHandler(UserRolesChangeEvent.getType(), this);

		FlowPanel accountChooserDetail = new FlowPanel();
		accountChooserDetail.setStyleName(style.cfcAccountchooserDetails());
		
		SimplePanel imgPanel = new SimplePanel();
		imgPanel.setStyleName(style.cfcProfilepicture(), true);
		imgPanel.setStyleName(style.ngStartInserted(), true);
		imgPanel.getElement().setTabIndex(-1);
		
		SimplePanel accountIcon = new SimplePanel();
		accountIcon.setStyleName(style.largeAccountIcon());
		accountIcon.add(new Icons.Login());

		imgPanel.add(accountIcon);
		accountChooserDetail.add(imgPanel);
		
		accountChooserMenu.add(accountChooserDetail);
		accountChooserDetail.add(new Label((String)user.getAuthorityInfo().getPrincipal()));

		FlowPanel accountchooserButton = new FlowPanel();
		accountchooserButton.setStyleName(style.cfcAccountchooserButtons(), true);
		accountchooserButton.setStyleName(style.cfcProfileRow(), true);

		Button b = new Button("\u8d26\u53f7\u4fe1\u606f");
		b.setStyleName(style.cfcProfilebutton());
		
		b.addClickHandler(c->{uiHandler.removeMenuPanel(); uiHandler.gotoPlace(NameTokens.security);});
		accountchooserButton.add(b);
		accountChooserMenu.add(accountchooserButton);


		b = new Button("\u9000\u51fa\u8d26\u53f7");
		b.setStyleName(style.cfcProfilebutton());
		
		b.addClickHandler(c->{uiHandler.removeMenuPanel(); user.Logout();});
		accountchooserButton.add(b);

		matIcon.appendChild(new Icons.Login().getElement());
    	Element el = button.getElement();
        el.setAttribute("aria-label", "\u6253\u5f00\u5e10\u53f7\u9009\u9879");
	}
	
	@Override
	public void onAttach() {
		super.onAttach();
		int rightPosition = Window.getClientWidth() - button.getAbsoluteLeft() - button.getOffsetWidth();
		boundingboxMenu.getElement().setAttribute("style", "top: 42px; right: " + rightPosition + "px; height: 100%; width: 100%; align-items: flex-end; justify-content: flex-start;");
	}
	
	@Override
	public Panel getMenuPanel() {
		if(user.isLogin())
			return boundingboxMenu;
		
		UrlBuilder builder = new UrlBuilder();
		builder.setProtocol(Window.Location.getProtocol())
		       .setHost(Window.Location.getHost())
		       .setPath("/login");

		String port = Window.Location.getPort();
		if (port != null && port.length() > 0) {
			builder.setPort(Integer.parseInt(port));
		}
		Window.Location.replace(builder.buildString());
		return null;
   	}

	@Override
	public String getTip() {

		if(user.isLogin())
			return "\u6253\u5f00\u5e10\u53f7\u9009\u9879:" + (String)user.getAuthorityInfo().getPrincipal();

		return "\u767b\u5f55\u7528\u6237";
	}

	@Override
	public void onUserStatusChange() {
		if(user.isLogin())
			GWT.log("should show login icon");
		else
			GWT.log("should show logout icon");
	}
}
