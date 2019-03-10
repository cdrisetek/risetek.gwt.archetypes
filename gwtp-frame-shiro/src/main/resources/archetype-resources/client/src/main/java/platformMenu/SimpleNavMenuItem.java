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

public class SimpleNavMenuItem extends FocusPanel {
    private final Style style = StyleBundle.resources.style();

	public SimpleNavMenuItem(@NotNull String name, String token, Element icon, Consumer<String> consumer) {
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
		Panel panel = new SimpleNavMenuItem("支持帮助", NameTokens.help, helpIcon(), consumer);
		return panel;
	}
	
    private static native Element helpIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 17h-1v5H8V11H3V8h9v9zm0-10c-1.65 0-3-1.35-3-3s1.35-3 3-3v6z");
        e.appendChild(p);
		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16 11v11h-3v-5h-1V8h9v3h-5zm-4-4V1c1.65 0 3 1.35 3 3s-1.35 3-3 3z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);
        return e;
    }-*/;
	
	public static Panel makeServerMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("服务状态", NameTokens.help, serverIcon(), consumer);
		return panel;
	}
    
    private static native Element serverIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M7 3h2v2H7zM3 7h2v2H3z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M19 7h2v2h-2z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M3 11h2v2H3z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M19 11h2v2h-2z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M3 15h2v2H3z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M19 15h2v2h-2zM7 19h2v2H7z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M11 3h2v2h-2z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M11 19h2v2h-2z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M15 3h2v2h-2z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M15 19h2v2h-2z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M5 19h14V5H5v14zm12-2H7V7h10v10z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M15 9H9v6l3-3z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 12l-3 3h6z");
        p.setAttribute("opacity", ".8");

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M15 15V9l-3 3z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);
        return e;
    }-*/;
    
	public static Panel makeAboutMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("关于", NameTokens.help, aboutIcon(), consumer);
		return panel;
	}
    
    private static native Element aboutIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M5.336 19.49a3.556 3.556 0 1 1-.002-7.112 3.556 3.556 0 0 1 .002 7.112zm4.365-.486c.6-.86.95-1.9.95-3.022a5.32 5.32 0 0 0-5.32-5.315 5.32 5.32 0 0 0-4.68 7.85 5.34 5.34 0 0 0 2.375 2.257c.698.336 1.48.524 2.307.524a5.3 5.3 0 0 0 3.02-.938l3.51 3.523a.4.4 0 0 0 .564.002l.8-.79a.398.398 0 0 0 0-.564L9.7 19.01z");
        p.setAttribute("opacity", ".8");
        p.setAttribute("fill-rule", "nonzero");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M11.76 17.493c.17-.573.263-1.18.263-1.806 0-3.59-3.023-6.502-6.752-6.502a6.95 6.95 0 0 0-2.3.39V4.852a1 1 0 0 1 1-1h18.92a1 1 0 0 1 1 1v11.64a1 1 0 0 1-1 1H11.76z");
        p.setAttribute("fill-rule", "nonzero");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16.296 3.852V2.074h-5.333V3.21l-1.778.642V1.296a1 1 0 0 1 1-1h6.89a1 1 0 0 1 1 1v2l-1.768.556h-.01z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16.296 3.86l1.78-.57v.57zm-7.128-.005l1.786-.645v.645z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);
        return e;
    }-*/;
    
	public static Panel makeConvertMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("编码转换", NameTokens.convert, ConvertIcon(), consumer);
		return panel;
	}
    
    private static native Element ConvertIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M5.327 13.1l-2.48 2.098L5.617 20h3.69z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M4.692 12l-1.846 3.198 2.48-2.1z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M2.846 15.198L9.31 4H5.62L1 12z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M18.673 10.9l2.48-2.098L18.383 4h-3.69z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M19.308 12l1.846-3.198-2.48 2.1z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M2.846 15.198L9.31 4H5.62L1 12z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M21.154 8.802L14.69 20h3.69L23 12z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16 12l-1.155-2h-5.69L8 12z");
        e.appendChild(p);
        return e;
    }-*/;
    

	public static Panel makeHomeMenuItem(Consumer<String> consumer) {
		Panel panel = new SimpleNavMenuItem("\u9996\u9875", NameTokens.home, homeIcon(), consumer);
		return panel;
	}
	
    private static native Element homeIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 3.15l-9 9.472 1.352 1.433L12 6.007l7.648 8.048L21 12.622z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 6.007l-7 7.366v2.132l7.475-9z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12.475 6.506l-.475.57V14l2 .002V21h5v-7.627z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M5 15.505V21h5v-7h2V7.077z");
        e.appendChild(p);
        return e;
    }-*/;
}
