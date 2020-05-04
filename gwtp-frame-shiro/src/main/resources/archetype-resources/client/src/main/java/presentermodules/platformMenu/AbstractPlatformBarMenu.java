package ${package}.presentermodules.platformMenu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.ViewImpl;

public abstract class AbstractPlatformBarMenu extends ViewImpl implements HasUiHandlers<MyUiHandlers> {
    @UiTemplate("Menu.ui.xml")
	public interface Binder extends UiBinder<HTMLPanel, AbstractPlatformBarMenu> {}

    public interface Style extends CssResource {
    	String matIconButton();
    	String cfcAccountchooserButtons();
    	String barButton();
    	String copyright();
    	String cfcNavchooserDetails();
    	String pccConsoleNavButton();
    	String cfcIcon();
    	String cfcProfilebutton();
    	String matIcon();
    	String cfcProfilepicture();
    	String largeAccountIcon();
    	String ngStartInserted();
    	String cfcAccountChooserMenu();
    	String cfcAccountchooserDetails();
    	String navBarContainer();
    	String navMenubottomContainer();
    	String cfcProfileRow();
    }
    public @UiField Style style;
    
    public @UiField Button button;
    
    public @UiField HTMLPanel boundingboxMenu, boundingboxTips, accountChooserMenu, NavButton;
    
    public @UiField Element matIcon, tooltip, overlaypanelTips, overlaypanelMenu;

    protected MyUiHandlers uiHandler;

    public AbstractPlatformBarMenu(Binder binder) {
    	initWidget(binder.createAndBindUi(this));
    	button.setTabIndex(0);
		button.sinkEvents(Event.ONMOUSEOVER);
    	button.addMouseOutHandler(c->{boundingboxTips.removeFromParent();});
    	boundingboxTips.getElement().setAttribute("style", "top: 0px; left: 0px; height: 100%; width: 100%;");
    }

    @UiHandler("button")
    void onButtonMouseOver(MouseOverEvent event) {
    	uiHandler.showTip(this);
    }
    
    @UiHandler("button")
    void onButtonClick(ClickEvent event) {
    	uiHandler.onMenuClick(this);
    }
    
	@Override
	public void setUiHandlers(MyUiHandlers uiHandler) {
    	this.uiHandler = uiHandler;
	}
	
	@Override
	public void onAttach() {
		int xpostion = button.getAbsoluteLeft();
		int windowWith = Window.getClientWidth();

		if(xpostion < windowWith/2)
			overlaypanelTips.setAttribute("style", "pointer-events: auto; top: 48px; left: " + xpostion + "px;");
		else
			overlaypanelTips.setAttribute("style", "pointer-events: auto; top: 48px; right: 12px;");

    	tooltip.setInnerText(getTip());
	}
	
    abstract public String getTip();
	abstract public Panel getMenuPanel();
    
	public Panel getIcon() {
        return NavButton;
	}
    
    public Panel getToolTipPanel() {
    	return boundingboxTips;
    }
}
