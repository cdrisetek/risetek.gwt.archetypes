package ${package}.presentermodules.platformMenu;

import javax.inject.Inject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import ${package}.NameTokens;
import ${package}.entry.Subject;
import ${package}.entry.SubjectChangeEvent;
import ${package}.entry.SubjectChangeEvent.SubjectChangeHandler;

@Singleton
public class SimpleLoginMenu extends AbstractPlatformBarMenu implements SubjectChangeHandler {
	private final Subject subject;
	private final ChooserPanel chooserPanel;

	@Inject
	public SimpleLoginMenu(final Binder binder,
			final Subject subject,
			final ChooserPanel chooserPanel,
			EventBus eventBus) {
		super(binder);
		
		this.subject = subject;
		setMenuIcon(chooserPanel.iconMenu);

		eventBus.addHandler(SubjectChangeEvent.getType(), this);
		this.chooserPanel = chooserPanel;
		panelChoosers.add(chooserPanel.asWidget());
	}
	
	@Override
	public void onAttach() {
		super.onAttach();
		chooserPanel.setUiHandlers(getUiHandlers());
		// TODO: adjust by button.
		int rightPosition = Window.getClientWidth() - btnIcon.getAbsoluteLeft() - btnIcon.getOffsetWidth();
		setChooserBoxRight(rightPosition);
	}
	
	@Override
	public Panel getChooserPanel() {
		if(subject.isLogin())
			return boundingboxMenu;

		subject.Login();
		return null;
   	}

	@Override
	public String getTipString() {

		if(subject.isLogin())
			return "\u6253\u5f00\u5e10\u53f7\u9009\u9879:" + subject.getSubjectPrincipal();

		return "\u767b\u5f55\u7528\u6237";
	}

	@Override
	public void onSubjectChange() {
		if(subject.isLogin())
			GWT.log("should show login icon");
		else
			GWT.log("should show logout icon");
	}

	static class ChooserPanel extends ViewWithUiHandlers<MyUiHandlers> {
		interface AccountBinder extends UiBinder<Widget, ChooserPanel> {}
		private final Subject subject;
		@UiField Label labelPrincipal;
		@UiField Element iconMenu;
		@Inject
		public ChooserPanel(final AccountBinder binder,
				final Subject subject
				) {
			initWidget(binder.createAndBindUi(this));
			this.subject = subject;
		}
		
		@UiHandler("btnAccount")
		public void onAccountClick(ClickEvent e) {
			getUiHandlers().hideChooser();
			getUiHandlers().gotoPlace(NameTokens.security);
		}
		
		@UiHandler("btnLogout")
		public void onLogoutClick(ClickEvent e) {
			getUiHandlers().hideChooser();
			subject.Logout();
		}
		
		@Override
		public void onAttach() {
			super.onAttach();

			if(subject.isLogin()) {
				String principal = subject.getSubjectPrincipal();
				labelPrincipal.setText((null == principal)?"unknow":principal);
			} else
				labelPrincipal.setText(null);
		}		
	}
}
