package ${package}.presentermodules.platformMenu;

import javax.inject.Inject;

import com.google.gwt.core.shared.GWT;
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
import ${package}.utils.TagWrapBuilder;

@Singleton
public class SimpleLoginMenu extends AbstractPlatformBarMenu implements UserRolesChangeHandler {

	private final CurrentUser user;
	
	@Inject
	public SimpleLoginMenu(CurrentUser user, EventBus eventBus) {
		this.user = user;
		eventBus.addHandler(UserRolesChangeEvent.getType(), this);
	}
	
	
	@Override
	public Panel getIcon() {
        Panel matIcon = new TagWrapBuilder(new Icons.Login(), style.matIcon())
		        .addStyleName(style.matIcon())
		        .build();

        Panel cfcIcon = new TagWrapBuilder(matIcon, style.cfcIcon())
		        .addStyleName(style.cfcIcon())
		        .addStyleName(style.ngStartInserted())
		        .build();
        
        Button button = mkButton("\u6253\u5f00\u5e10\u53f7\u9009\u9879", cfcIcon.getElement());
        button.setStyleName(style.barButton());
        button.setStyleName(style.matIconButton(), true);
        
    	return new TagWrapBuilder(button, style.pccConsoleNavButton())
    			   .addStyleName(style.pccConsoleNavButton())
    			   .build();
	}
	
	@Override
	public Panel getMenuPanel() {
		if(!user.isLogin()) {
			UrlBuilder builder = new UrlBuilder();
			builder.setProtocol(Window.Location.getProtocol());
			builder.setHost(Window.Location.getHost());
			String port = Window.Location.getPort();
			if (port != null && port.length() > 0) {
				builder.setPort(Integer.parseInt(port));
			}
			builder.setPath("/login");
			Window.Location.replace(builder.buildString());
			return null;
		}
		int rightPosition = Window.getClientWidth() - getAbsoluteLeft() - getOffsetWidth();
		Panel position = mkPositionBoundingBox();
    	position.getElement().setAttribute("style", "top: 42px; right: " + rightPosition + "px; height: 100%; width: 100%; align-items: flex-end; justify-content: flex-start;");
		
    	Panel container = new SimplePanel();
		container.setStyleName(style.cdkOverlayPane());
		container.getElement().setPropertyString("style", "pointer-events: auto; position: static;");
		
		FlowPanel accountChooserMenu = new FlowPanel();
		accountChooserMenu.setStyleName(style.cfcAccountChooserMenu(), true);
		accountChooserMenu.setStyleName(style.ngStartInserted(), true);

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

		container.add(accountChooserMenu);
		
		position.add(container);
		return position;
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
