package ${package}.presentermodules.platformMenu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public abstract class AbstractPlatformBarMenu extends ViewWithUiHandlers<MyUiHandlers> {
    @UiTemplate("Menu.ui.xml")
	public interface Binder extends UiBinder<Widget, AbstractPlatformBarMenu> {}

    @UiField Button btnIcon;
    
    @UiField HTMLPanel boundingboxMenu, boundingboxTips, panelChoosers, panelIcon;
    
    @UiField Element iconBox, tooltip, panelTips, panelMenu;

    public AbstractPlatformBarMenu(final Binder binder) {
    	initWidget(binder.createAndBindUi(this));
    	btnIcon.setTabIndex(0);
		btnIcon.sinkEvents(Event.ONMOUSEOVER);
    	btnIcon.addMouseOutHandler(c->{boundingboxTips.removeFromParent();});
    }

    @UiHandler("btnIcon")
    void onButtonMouseOver(MouseOverEvent event) {
    	getUiHandlers().showTip(this);
    }
    
    @UiHandler("btnIcon")
    void onButtonClick(ClickEvent event) {
    	getUiHandlers().onIconClick(this);
    }
    
	@Override
	public void onAttach() {
		int xpostion = btnIcon.getAbsoluteLeft();
		int windowWith = Window.getClientWidth();

		if(xpostion < windowWith/2)
			panelTips.setAttribute("style", "left: " + xpostion + "px;");
		else
			panelTips.setAttribute("style", "right: 12px;");

    	tooltip.setInnerText(getTipString());
	}
	
    abstract public String getTipString();
	abstract public Panel getChooserPanel();
    
	void setMenuIcon(Element icon) {
		iconBox.appendChild(icon);
	}

	public Panel getIconPanel() {
        return panelIcon;
	}
    
    public Panel getTipPanel() {
    	return boundingboxTips;
    }
    
    public void setChooserBoxRight(int position) {
		boundingboxMenu.getElement().setAttribute("style", "right: " + position + "px; align-items: flex-end;");
    }

    public void setChooserBoxLeft(int position) {
		boundingboxMenu.getElement().setAttribute("style", "left: " + position + "px; align-items: flex-start;");
    }
}
