package ${package}.presentermodules.platformMenu;

import java.util.List;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.risetek.bindery.IBuilderStamp;
import com.risetek.bindery.PlainMenuLoader;
import com.risetek.bindery.PlainMenuLoader.PlainMenuContent;

@Singleton
public class SimpleNavMenu extends AbstractPlatformBarMenu {
	private final static PlainMenuLoader menuloader = GWT.create(PlainMenuLoader.class);
	private final ChooserPanel chooserPanel;
	@Override
	public String getTipString() {
		return "\u5bfc\u822a\u83dc\u5355";
	}

	@Inject
	public SimpleNavMenu(final Binder binder, final ChooserPanel chooserPanel) {
		super(binder);
		this.chooserPanel = chooserPanel;
//		Scheduler.get().scheduleDeferred(() -> chooserPanel.setUiHandlers(getUiHandlers()));
		setMenuIcon(chooserPanel.iconMenu);
		setChooserBoxLeft(20);

		panelChoosers.add(chooserPanel);

		List<PlainMenuContent> menus = menuloader.getMenus();
		
		FlowPanel navBarContainer = null;
		int index = 0;
		for(PlainMenuContent menu: menus) {
			if(index++ % 3 == 0) {
				navBarContainer = new FlowPanel();
				chooserPanel.add(navBarContainer);
			}
				
			Panel panel = new SimpleNavMenuItem(menu.title, menu.token, menu.icon == null ? null : menu.icon.getElement(), c->{getUiHandlers().gotoPlace(c);});
			navBarContainer.add(panel);
		}
	}

	@Override
	public void onAttach() {
        super.onAttach();
		chooserPanel.setUiHandlers(getUiHandlers());
	}

	@Override
	public Panel getChooserPanel() {
		return boundingboxMenu;
	}

	static class ChooserPanel extends ViewWithUiHandlers<MyUiHandlers> {
		private final static IBuilderStamp stamp = GWT.create(IBuilderStamp.class);
		private static String copyrightText = "© 2000-" + stamp.getYear() + " Chengdu Risetek Corp.  &nbsp;&nbsp;&nbsp;Build at:" + stamp.getBuilderStamp();

		interface AccountBinder extends UiBinder<Widget, ChooserPanel> {}
		@UiField FlowPanel navChooserDetail;
		@UiField DivElement labelCopyright;
		@UiField Element iconMenu;
		@Inject
		public ChooserPanel(final AccountBinder binder) {
			initWidget(binder.createAndBindUi(this));
			labelCopyright.setInnerHTML(copyrightText);
		}
		
		public void add(Panel panel) {
			navChooserDetail.add(panel);
		}
		
	}
}
