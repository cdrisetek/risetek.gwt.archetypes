package ${package}.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeViewImpl implements HomeView {

	private final Widget w;

	private Presenter presenter;

	@Inject
	HomeViewImpl() {
		SimplePanel sp = new SimplePanel();
		FlowPanel fp = new FlowPanel();
		sp.add(fp);
		fp.add(new HTML("This is line one"));
		fp.add(new HTML("This is line two"));
		fp.add(new HTML("This is line three"));
		fp.add(new HTML("This is line four"));
		w = sp;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return w;
	}
}
