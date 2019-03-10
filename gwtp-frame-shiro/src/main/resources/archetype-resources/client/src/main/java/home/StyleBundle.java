package ${package}.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	public static final StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String homeStyle();
		public String startInserted();
		public String panelBody();
		public String panelBodyScrollable();
		public String panelBodyScrollContent();
		public String infoCardContainer();
		public String infoCardContainerCenter();
		public String infoCardContainerCenterNoWrap();
		public String infoCardColumn();
		public String infoCardColumnWidth1();
		public String infoCard();
		public String infoCardStyling();
		public String infoCardTopRight();
		public String infoCardHeader();
		public String headContent();
		public String headText();
		public String headMetadata();
		public String cfcLoader();
		public String loaderContent();
		public String cardBodyGroup();
		public String infoTextLine();
		public String cardInfoText();
		public String cardInfoTextSecondary();
		public String infoCardItem();
		public String infoCardItemLeftGutter();
		public String InfoCardItemContent();
		public String infoCardItemContentLeft();
		public String matIcon();
		public String cardRedirect();
	}
	
	@Source("style.gss")
	Style style();
}
