package ${package}.platformMenu;

import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import ${package}.NameTokens;
import ${package}.platformMenu.StyleBundle.Style;
import ${package}.utils.Icons;

public class SimpleNavMenuItem extends FocusPanel {
    private final Style style = StyleBundle.resources.style();

	public SimpleNavMenuItem(@NotNull String name, @NotNull String token, Element icon, @NotNull Consumer<String> consumer) {
		style.ensureInjected();
		FlowPanel navItem = new FlowPanel();
		navItem.setStyleName(style.navItem());
		SimplePanel iconContainer = new SimplePanel();
		iconContainer.setStyleName(style.menuIconContainer());
		if(null != icon) {
			iconContainer.getElement().appendChild(icon);
		}
		navItem.add(iconContainer);
		Label n = new Label(name);
		n.setStyleName(style.menuItemTitle());
		navItem.add(n);
		add(navItem);
		if(null == token)
			addClickHandler(c->{consumer.accept(name);});
		else
			addClickHandler(c->{consumer.accept(token);});
	}

	public static Panel makeHelpMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("\u652f\u6301\u5e2e\u52a9", NameTokens.help, Icons.helpIcon(), consumer);
		return panel;
	}
	
	public static Panel makeServerMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("服务状态", NameTokens.help, Icons.serverIcon(), consumer);
		return panel;
	}

	public static Panel makeAboutMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("关于", NameTokens.help, Icons.aboutIcon(), consumer);
		return panel;
	}
    
	public static Panel makeConvertMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("\u7f16\u7801\u8f6c\u6362", NameTokens.convert, Icons.convertIcon(), consumer);
		return panel;
	}

	public static Panel makeHomeMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("\u9996\u9875", NameTokens.home, Icons.homeIcon(), consumer);
		return panel;
	}
}
