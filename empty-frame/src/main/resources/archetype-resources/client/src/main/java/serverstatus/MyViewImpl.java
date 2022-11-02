package ${package}.serverstatus;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

@Singleton
public class MyViewImpl implements ServerStatusView {

	private final Panel panel;

	private Presenter presenter;
	
	private FlowPanel flowPanel;
	
	private Button goHome = new Button("Home");

	private final MyBundle.Style style = MyBundle.resources.style();

	@Inject
	MyViewImpl() {
		style.ensureInjected();
		goHome.addClickHandler(c-> presenter.gotoHome());
		
		panel = new SimplePanel();
		panel.getElement().setPropertyString("align", "center");
		
		
		flowPanel = new FlowPanel();
		panel.add(flowPanel);
		flowPanel.setStyleName(style.cards());

		flowPanel.addAttachHandler(c->{
			flowPanel.clear();
			flowPanel.add(goHome);
			presenter.updateStatus();
		});
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return panel;
	}

	private class Card extends FlowPanel {
		public Card(String title, String message) {
			style.ensureInjected();
			setStyleName(style.card());
			
			Label titleL = new Label(title);
			add(titleL);
			titleL.addStyleName(style.title());

			Label messageL = new Label(message);
			add(messageL);
	
		}
	}

	@Override
	public void appendStatus(String title, String message) {
		flowPanel.add(new Card(title, message));
	}

}
