package ${package}.platformMenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	
	static StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String barContainer();
		public String barBlue();
		public String platformBar();
		public String barLeft();
		public String barMiddle();
		public String barRight();
		public String barButton();
		public String matIconButton();
		public String navButton();
		public String cfcSizeMedium();
		public String cfcIcon();
		public String matIcon();
		public String ngStartInserted();
		public String cfcAccountChooserLink();
		public String cfcAccountChooserMenu();
		public String cfcAccountchooserDetails();
		public String cfcChooserDynamic();
		public String pccConsoleNavButton();
		public String matButton();
		public String matFlatButton();
		public String matStrokedButton();
		public String cdcOverlayContainer();
		public String cdcGlobalOverlayWrapper();
		public String cfcNg2Region();
		public String cdkOverlayPane();
		public String cfcTooltipOverlaySimple();
		public String cdkOverlayConnectedPositionBoundingBox();
		public String cdkOverlayTransparentBackdrop();
		public String cdkOverlayBackdropShowing();
		public String cdkOverlayBackdrop();
		public String cfcProfilepicture();
		public String cfcAccountchooserButtons();
		public String cfcProfilebutton();
		public String cfcProfileRow();
		public String largeAccountIcon();
		public String navItem();
		public String cfcNavchooserDetails();
		public String navBarContainer();
		public String navMenubottomContainer();
		public String copyright();
		public String menuItemTitle();
		public String menuIconContainer();
	}
	
	@Source("style.gss")
	Style style();
}
