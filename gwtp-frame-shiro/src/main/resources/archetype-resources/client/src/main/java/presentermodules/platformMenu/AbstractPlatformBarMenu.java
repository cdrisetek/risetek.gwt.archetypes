package ${package}.presentermodules.platformMenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.HasUiHandlers;
import ${package}.presentermodules.platformMenu.StyleBundle.Style;
import ${package}.utils.TagWrapBuilder;

public abstract class AbstractPlatformBarMenu extends Button implements HasUiHandlers<MyUiHandlers> {
    public final Style style = StyleBundle.resources.style();
    protected MyUiHandlers uiHandler;

    public AbstractPlatformBarMenu() {
    	style.ensureInjected();
		setStyleName(style.cfcAccountChooserLink());
    	setTabIndex(0);

    	addMouseOverHandler(c->{uiHandler.showTip(this);});
		addClickHandler(c->{uiHandler.onMenuClick(this);});
    }
    
	@Override
	public void setUiHandlers(MyUiHandlers uiHandler) {
    	this.uiHandler = uiHandler;
	}
	
    abstract public String getTip();
	abstract public Panel getMenuPanel();
    abstract public Panel getIcon();
    
    Button mkButton(String label, Element child) {
    	Element el = getElement();
        el.setAttribute("aria-label", label);
        el.setAttribute("aria-haspopup", "true");
        el.appendChild(child);
    	return this;
    }
    
    Panel mkPositionBoundingBox() {
    	Panel position = new SimplePanel();
    	position.getElement().setDir("ltr");
    	position.setStyleName(style.cdkOverlayConnectedPositionBoundingBox());
    	return position;
    }
    
    Panel getToolTipPanel() {
		style.ensureInjected();
    	Panel position = mkPositionBoundingBox();
    	position.getElement().setAttribute("style", "top: 0px; left: 0px; height: 100%; width: 100%;");

    	Element overlay = DOM.createDiv();
    	overlay.setClassName(style.cdkOverlayPane());
    	
    	// TODO: we should compute the position.
		int xpostion = getAbsoluteLeft();
		int windowWith = Window.getClientWidth();
		boolean end;
		if(xpostion < windowWith/2)
			end = false;
		else
			end = true;
		GWT.log("show at: " + xpostion + " with:" + windowWith);
    	if(end)
        	overlay.setAttribute("style", "pointer-events: auto; top: 48px; right: 12px;");
    	else
    		overlay.setAttribute("style", "pointer-events: auto; top: 48px; left: " + xpostion + "px;");

    	Panel tooltip =  new TagWrapBuilder("cfcTooltipOverlay")
    			             .addStyleName(style.cfcTooltipOverlaySimple())
    			             .build();

    	tooltip.getElement().setAttribute("role", "tooltip");
    	tooltip.getElement().setInnerText(getTip());

    	overlay.appendChild(tooltip.getElement());
    	position.getElement().appendChild(overlay);
    	return position;
    }
}
