package ${package}.presentermodules.platformMenu;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import ${package}.bindery.IBuilderStamp;
import ${package}.utils.Icons;
import ${package}.utils.TagWrapBuilder;

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
		Panel matIcon = new TagWrapBuilder(Icons.matIcon(), style.matIcon())
				            .addStyleName(style.matIcon())
				            .build();
		Panel cfcIcon = new TagWrapBuilder(matIcon, style.cfcIcon())
				            .addStyleName(style.cfcIcon())
				            .addStyleName(style.ngStartInserted())
				            .build();
		
        Button button = mkButton("\u5bfc\u822a\u83dc\u5355", cfcIcon.getElement());
        button.setStyleName(style.barButton());
        button.setStyleName(style.matIconButton(), true);
    	
    	return new TagWrapBuilder(button, style.pccConsoleNavButton())
    			   .addStyleName(style.pccConsoleNavButton())
    			   .build();
	}
}
