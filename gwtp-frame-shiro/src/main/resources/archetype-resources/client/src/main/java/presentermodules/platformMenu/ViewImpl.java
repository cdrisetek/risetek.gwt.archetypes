package ${package}.presentermodules.platformMenu;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.presentermodules.platformMenu.StyleBundle.Style;

public class ViewImpl extends ViewWithUiHandlers<MyUiHandlers> implements PagePresenter.MyView {
    private final Style style = StyleBundle.resources.style();
    
    // tips container
    private final Panel overlayContainer = mkCDCOverlayContainer();
	private final Panel backdrop = new SimplePanel();
	private final FlowPanel barContainer = new FlowPanel();
    
    @Inject
    public ViewImpl() {
		style.ensureInjected();
		// mask panel for menu operator
		backdrop.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().removeMenuPanel();
			}}, ClickEvent.getType());

		backdrop.setStyleName(style.cdkOverlayBackdrop(), true);
		backdrop.setStyleName(style.cdkOverlayTransparentBackdrop(), true);
		backdrop.setStyleName(style.cdkOverlayBackdropShowing(), true);

        // bar Container
        barContainer.add(overlayContainer);
        barContainer.setStyleName(style.barContainer());
        barContainer.setStyleName(style.barBlue(), true);
        barContainer.setStyleName(style.platformBar(), true);
        barContainer.setStyleName(style.ngStartInserted(), true);
        barContainer.getElement().setAttribute("aria-label","\u5168\u5c40\u5de5\u5177\u680f");
        initWidget(barContainer);
    }

    /**
     * show menu tips
     * @return
     */
    private Panel mkCDCOverlayContainer() {
		style.ensureInjected();
    	Panel container = new FlowPanel();
    	container.setStyleName(style.cdcOverlayContainer(), true);
    	container.setStyleName(style.cfcNg2Region(), true);
    	return container;
    }

	@Override
	public void removeTip(AbstractPlatformBarMenu menu) {
		overlayContainer.remove(menu);
	}
	
	private Panel menuPanel = null;

	@Override
	public void removeMenuPanel() {
		overlayContainer.remove(backdrop);
		if(menuPanel == null)
			return;
		overlayContainer.remove(menuPanel);
		menuPanel = null;
	};

	@Override
	public void showMenuPanel(AbstractPlatformBarMenu menu) {
		if(menuPanel != null)
			return;

		Panel panel = menu.getMenuPanel();
		if(null == panel)
			return;

		menuPanel = panel;

		overlayContainer.add(backdrop);
		overlayContainer.add(menuPanel);
	}

	@Override
	public void showTip(AbstractPlatformBarMenu menu) {
		menu.sinkEvents(Event.ONMOUSEOVER);
		Panel tipPanel = menu.getToolTipPanel();
		menu.addMouseOutHandler(c->{overlayContainer.remove(tipPanel);});
    	overlayContainer.add(tipPanel);
	}

	private void installMenu(String style, AbstractPlatformBarMenu... menus) {
    	assert(null != getUiHandlers());
    	FlowPanel container = new FlowPanel();
    	container.setStyleName(style);

    	for(AbstractPlatformBarMenu menu:menus) {
	    	menu.setUiHandlers(getUiHandlers());
	    	container.add(menu.getIcon());
    	}
		barContainer.add(container);
	}
	
	@Override
	public void installRightMenu(AbstractPlatformBarMenu... menus) {
		installMenu(style.barRight(), menus);
	}

	@Override
	public void installMiddleMenu(AbstractPlatformBarMenu... menus) {
		installMenu(style.barMiddle(), menus);
	}

	@Override
	public void installLeftMenu(AbstractPlatformBarMenu... menus) {
		installMenu(style.barLeft(), menus);
	}
}
