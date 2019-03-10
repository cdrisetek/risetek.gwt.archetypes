package ${package}.platformMenu;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risetek.generator.IBuilderStamp;

@Singleton
public class SimpleNavMenu extends AbstractPlatformBarMenu {
	private static String copyrightText = "© 2000-2019 Chengdu Risetek Corp.  &nbsp;&nbsp;&nbsp;Build at:" + 
            ((IBuilderStamp)GWT.create(IBuilderStamp.class)).getBuilderStamp();
	@Override
	public String getTip() {
		return "\u5bfc\u822a\u83dc\u5355";
	}

	@Override
	public Panel getMenuPanel() {
		Panel position = mkPositionBoundingBox();
    	position.getElement().setAttribute("style", "top: 42px; left: 20px; height: 100%; width: 100%; align-items: flex-start; justify-content: flex-start;");
		
    	Panel container = new SimplePanel();
		container.setStyleName(style.cdkOverlayPane());
		container.getElement().setPropertyString("style", "pointer-events: auto; position: static;");
		
		FlowPanel accountChooserMenu = new FlowPanel();
		accountChooserMenu.setStyleName(style.cfcAccountChooserMenu(), true);
		accountChooserMenu.setStyleName(style.ngStartInserted(), true);

		FlowPanel navChooserDetail = new FlowPanel();
		navChooserDetail.setStyleName(style.cfcNavchooserDetails());
		accountChooserMenu.add(navChooserDetail);
		container.add(accountChooserMenu);
		position.add(container);

		FlowPanel navBarContainer = new FlowPanel();
		navBarContainer.setStyleName(style.navBarContainer());
		navBarContainer.add(SimpleNavMenuItem.makeHomeMenuItem(c->{uiHandler.gotoPlace(c);}));
		navBarContainer.add(SimpleNavMenuItem.makeHelpMenuItem(c->{uiHandler.gotoPlace(c);}));
		navBarContainer.add(new SimpleNavMenuItem("more 3", null, null, c->{uiHandler.gotoPlace(c);}));
		navChooserDetail.add(navBarContainer);
		
		navBarContainer = new FlowPanel();
		navBarContainer.setStyleName(style.navBarContainer());
//		navBarContainer.add(SimpleNavMenuItem.makeAboutMenuItem(c->{uiHandler.gotoPlace(c);}));
		navBarContainer.add(new SimpleNavMenuItem("more 2", null, null, c->{uiHandler.gotoPlace(c);}));
		navBarContainer.add(SimpleNavMenuItem.makeServerMenuItem(c->{uiHandler.gotoPlace(c);}));
		navBarContainer.add(SimpleNavMenuItem.makeConvertMenuItem(c->{uiHandler.gotoPlace(c);}));
		navChooserDetail.add(navBarContainer);

		// bottom bar
		FlowPanel bottomContainer = new FlowPanel();
		bottomContainer.setStyleName(style.navMenubottomContainer());
		HTML copyright = new HTML(copyrightText);
		copyright.setStyleName(style.copyright());
		bottomContainer.add(copyright);
		navChooserDetail.add(bottomContainer);
		return position;
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
        matIcon.getElement().appendChild(makeMatIcon());
        
        cfcIcon.add(matIcon);
        
        Button button = mkButton("导航菜单", cfcIcon.getElement());
        button.setStyleName(style.barButton());
        button.setStyleName(style.matIconButton(), true);
        placeholderButton.add(button);
    	return placeholderButton;
	}

    private static native Element makeMatIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z");
        e.appendChild(p);
        return e;
    }-*/;
}
