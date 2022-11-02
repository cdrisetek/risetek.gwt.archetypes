package ${package}.home;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import ${package}.generator.IBuilderStamp;
import ${package}.greeting.GreetingPlace;
import ${package}.login.LoginActivity.LoginPlace;
import ${package}.serverstatus.ServerStatusActivity.ServerStatusPlace;


@Singleton
public class HomeViewImpl implements HomeView {

	private final SimplePanel homeViewPanel;

	private Presenter presenter;

	private final MyBundle.Style style = MyBundle.resources.style();

	// Blueboard hide when login;
	private final Blueboard loginBoard;
	private final Blueboard logoutBoard;

	@Inject
	HomeViewImpl() {
		style.ensureInjected();
		
		homeViewPanel = new SimplePanel();
		homeViewPanel.getElement().setPropertyString("align", "center");

		FlowPanel fp = new FlowPanel();
		fp.setStyleName(style.note_items());
		homeViewPanel.add(fp);
		
		fp.add(new Blueboard("Build at: " + ((IBuilderStamp)GWT.create(IBuilderStamp.class)).getBuilderStamp()));
        fp.add(new Blueboard("Use of CSS & GSS"));
        fp.add(new Blueboard("Use of Place", new GreetingPlace("whois")));
        fp.add(new Blueboard("Use of Dagger"));
        fp.add(new Blueboard("Use of requestFactory", new ServerStatusPlace()));
        fp.add(new Blueboard("Menu Design"));
		loginBoard = new Blueboard("UI for Login", new LoginPlace());
		logoutBoard = new Blueboard("Logout");
		logoutBoard.addClickHandler(c -> {
			presenter.logout();
		});
		fp.add(loginBoard);
		fp.add(logoutBoard);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return homeViewPanel;
	}

	private class Blueboard extends HorizontalPanel implements HasClickHandlers {
		public Blueboard(String text) {
			this(text, null);
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}

		public Blueboard(String text, Place place) {
			style.ensureInjected();
			setStyleName(style.blueboard());
			setVerticalAlignment(ALIGN_MIDDLE);

			Label textLabel = new Label(text);
			textLabel.setStyleName(style.note_item());
			add(textLabel);
			if(null != place) {
				Label go = new ClickGo(place);
				add(go);
				setCellWidth(go, "160px");
				setCellHorizontalAlignment(go, ALIGN_RIGHT);
			}
		}
	}
	
	private class ClickGo extends Label {
		public ClickGo(final Place place) {
			super("Go>>");
			style.ensureInjected();
			setStyleName(style.go());
			addClickHandler(c -> {
				presenter.gotoPlace(place);
			});
		}
	}

	@Override
	public void showLogin(boolean on) {
		if (on) {
			loginBoard.getElement().getStyle().clearDisplay();
			logoutBoard.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			loginBoard.getElement().getStyle().setDisplay(Display.NONE);
			logoutBoard.getElement().getStyle().clearDisplay();
		}
	}
}
