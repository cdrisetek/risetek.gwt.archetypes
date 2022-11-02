package ${package}.presentermodules.platformMenu;

import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class SimpleNavMenuItem extends FocusPanel {
    @UiTemplate("MenuItem.ui.xml")
	interface Binder extends UiBinder<HTMLPanel, SimpleNavMenuItem> {}
    private static final Binder binder = GWT.create(Binder.class);
    
    @UiField DivElement iconContainer;
    @UiField Label title;

	public SimpleNavMenuItem(@NotNull String name, @NotNull String token, Element icon, @NotNull Consumer<String> consumer) {
		add(binder.createAndBindUi(this));
		
		if(null != icon)
			iconContainer.appendChild(icon);

		title.setText(name);

		if(null == token)
			addClickHandler(c->{consumer.accept(name);});
		else
			addClickHandler(c->{consumer.accept(token);});
	}
}
