package ${package}.security.password;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface StyleBundle extends ClientBundle {
	public static final StyleBundle resources = GWT.create(StyleBundle.class);
	
	interface Style extends CssResource {
		public String containerStyle();
		public String infoLists();
		public String headTitle0();
		public String headTitle1();
		public String headTitleContent();
		public String headTitleBack();
		public String headTitleText();
		public String headTitleSeparator();
		public String wrapBackIcon();
		public String centerIcon();
		public String mainFlow();
		public String mainNote();
		public String vSpace();
		
		public String inputFrame();
		public String inputArea();
		public String inputCommit();
		
		public String inputAreaWiz();
		
		public String commitButton();
		public String commitButtonDown();
		public String buttonContent();
		public String buttonText();
		
		public String commitTips();
		public String commitTipsTitle();
		public String commitTipsTitleContent();
		public String commitTipsContent();
		
		public String passwordInput();
		public String passwordInputFocus();
		public String passwordInputNotEmpty();
		public String passwordInvalid();
		public String inputHolder();
		public String inputWrap();
		public String inputWrapHeight();
		public String inputWithEyes();
		
		public String inputUnderLine0();
		public String inputUnderLine1();
		public String inputLevel0();
		public String inputLevel1();
		public String inputControl();
		public String inputControlPad();
		
		public String buttonEye();
		public String buttonEyeWrap();
	}
	
	@Source("style.gss")
	Style style();
}
