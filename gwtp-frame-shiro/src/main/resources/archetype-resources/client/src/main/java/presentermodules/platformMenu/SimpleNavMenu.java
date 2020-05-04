package ${package}.presentermodules.platformMenu;

import javax.inject.Singleton;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import ${package}.bindery.IBuilderStamp;
import ${package}.bindery.PlainMenuLoader;
import ${package}.bindery.PlainMenuLoader.PlainMenuContent;
import ${package}.utils.Icons;

@Singleton
public class SimpleNavMenu extends AbstractPlatformBarMenu {
	private final static PlainMenuLoader menuloader = GWT.create(PlainMenuLoader.class);
	private final static IBuilderStamp stamp = GWT.create(IBuilderStamp.class);
	private static String copyrightText = "© 2000-" + stamp.getYear() + " Chengdu Risetek Corp.  &nbsp;&nbsp;&nbsp;Build at:" + stamp.getBuilderStamp();
	@Override
	public String getTip() {
		return "\u5bfc\u822a\u83dc\u5355";
	}

	@Inject
	public SimpleNavMenu(Binder binder) {
		super(binder);
		
		FlowPanel navChooserDetail = new FlowPanel();
		navChooserDetail.setStyleName(style.cfcNavchooserDetails());
		accountChooserMenu.add(navChooserDetail);

		List<PlainMenuContent> menus = menuloader.getMenus();
		
		FlowPanel navBarContainer = null;
		int index = 0;
		for(PlainMenuContent menu: menus) {
			if(index++ % 3 == 0) {
				navBarContainer = new FlowPanel();
				navBarContainer.setStyleName(style.navBarContainer());
				navChooserDetail.add(navBarContainer);
			}
				
			Panel panel = new SimpleNavMenuItem(menu.title, menu.token, menu.icon == null ? null : menu.icon.getElement(), c->{uiHandler.gotoPlace(c);});
			navBarContainer.add(panel);
		}
		
		// bottom bar
		FlowPanel bottomContainer = new FlowPanel();
		bottomContainer.setStyleName(style.navMenubottomContainer());
		HTML copyright = new HTML(copyrightText);
		copyright.setStyleName(style.copyright());
		bottomContainer.add(copyright);
		navChooserDetail.add(bottomContainer);
		
		boundingboxMenu.getElement().setAttribute("style", "top: 42px; left: 20px; height: 100%; width: 100%; align-items: flex-start; justify-content: flex-start;");

		matIcon.appendChild(Icons.matIcon());
    	Element el = button.getElement();
        el.setAttribute("aria-label", "\u5bfc\u822a\u83dc\u5355");
        el.setAttribute("aria-haspopup", "true");
	}

	@Override
	public Panel getMenuPanel() {
		return boundingboxMenu;
	}
}
