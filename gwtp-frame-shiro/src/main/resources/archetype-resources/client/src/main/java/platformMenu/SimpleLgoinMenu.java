package ${package}.platformMenu;

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
import ${package}.entry.CurrentUser;
import ${package}.entry.UserRolesChangeEvent;
import ${package}.entry.UserRolesChangeEvent.UserRolesChangeHandler;

@Singleton
public class SimpleLgoinMenu extends AbstractPlatformBarMenu implements UserRolesChangeHandler {

	private final CurrentUser user;
	
	@Inject
	public SimpleLgoinMenu(CurrentUser user, EventBus eventBus) {
		this.user = user;
		eventBus.addHandler(UserRolesChangeEvent.getType(), this);
	}
	
	
	@Override
	public Panel getIcon() {
		Panel placeholderButton = new UserComplexPanel("cfc-placeholder-button");
        placeholderButton.setStyleName(style.pccConsoleNavButton());
        
        Panel cfcIcon = new UserComplexPanel("cfc-icon");
        cfcIcon.setStyleName(style.cfcIcon(), true);
        cfcIcon.setStyleName(style.ngStartInserted(), true);

        Panel matIcon = new UserComplexPanel("mat-icon");
        matIcon.setStyleName(style.matIcon());
        matIcon.getElement().appendChild(makeLoginIcon());
        
        cfcIcon.add(matIcon);
        
        Button button = mkButton("\u6253\u5f00\u5e10\u53f7\u9009\u9879", cfcIcon.getElement());
        button.setStyleName(style.barButton());
        button.setStyleName(style.matIconButton(), true);
        placeholderButton.add(button);
    	return placeholderButton;
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
		accountIcon.getElement().appendChild(makeLoginIcon());

		imgPanel.add(accountIcon);
		accountChooserDetail.add(imgPanel);
		
		accountChooserMenu.add(accountChooserDetail);
		accountChooserDetail.add(new Label(user.getAuthorityInfo().getUsername()));

		FlowPanel accountchooserButton = new FlowPanel();
		accountchooserButton.setStyleName(style.cfcAccountchooserButtons(), true);
		accountchooserButton.setStyleName(style.cfcProfileRow(), true);

		Button b = new Button("\u9000\u51fa\u8d26\u53f7");
		b.setStyleName(style.cfcProfilebutton());
		
		b.addClickHandler(c->{uiHandler.removeMenuPanel(); user.Logout();});
		accountchooserButton.add(b);
		accountChooserMenu.add(accountchooserButton);

		container.add(accountChooserMenu);
		
		position.add(container);
		return position;
   	}

	@Override
	public String getTip() {

		if(user.isLogin())
			return "\u6253\u5f00\u5e10\u53f7\u9009\u9879:" + user.getAuthorityInfo().getUsername();

		return "\u767b\u5f55\u7528\u6237";
	}

    private static native Element makeLoginIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 512 512");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "m345.6,305c-28.7,0 -42.5,16 -89.6,16c-47.1,0 -60.8,-16 -89.6,-16"
                          + "c-74.2,0 -134.4,60.2 -134.4,134.4l0,25.6c0,26.5 21.5,48 48,48"
                          + "l352,0c26.5,0 48,-21.5 48,-48l0,-25.6c0,-74.2 -60.2,-134.4 -134.4,-134.4z"
                          + "m86.4,160l-352,0l0,-25.6c0,-47.6 38.8,-86.4 86.4,-86.4c14.6,0 38.3,16 89.6,16"
                          + "c51.7,0 74.9,-16 89.6,-16c47.6,0 86.4,38.8 86.4,86.4l0,25.6zm-176,-176"
                          + "c79.5,0 144,-64.5 144,-144s-64.5,-144 -144,-144s-144,64.5 -144,144"
                          + "s64.5,144 144,144zm0,-240c52.9,0 96,43.1 96,96s-43.1,96 -96,96"
                          + "s-96,-43.1 -96,-96s43.1,-96 96,-96z");
        e.appendChild(p);
        return e;
    }-*/;
    
	@Override
	public void onUserStatusChange() {
		if(user.isLogin())
			GWT.log("should show login icon");
		else
			GWT.log("should show logout icon");
	}
}
