package ${package}.home;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class PageView extends ViewWithUiHandlers<MyUiHandlers> implements
		PagePresenter.MyView {

	private final StyleBundle.Style style = StyleBundle.resources.style();

	private FlowPanel column1;
	private FlowPanel column2;
	private FlowPanel column3;
	
	@Inject
	public PageView() {
		style.ensureInjected();
		SimplePanel container = new SimplePanel();
		container.setStyleName(style.homeStyle());
		initWidget(container);
		
		FlowPanel cardContainer = new InfoCard.ContainerBuilder().setParent(container).build();
		
		column1 = new InfoCard.ColumnBuilder().setParent(cardContainer).build();
		column2 = new InfoCard.ColumnBuilder().setParent(cardContainer).build();
		column3 = new InfoCard.ColumnBuilder().setParent(cardContainer).build();

//		new InfoCard.Builder().setTitle("card1").setParent(column2).build();
	}

	@Override
	public InfoCard appendServerStateCard() {
		return new InfoCard.Builder().setTitle("服务状态")
				.setIcon(serverIcon()).setParent(column3)
				.build();
	}
	
	@Override
	public InfoCard appendWelcomeCard() {
		return new InfoCard.Builder().setTitle("欢迎使用")
				.setIcon(loginIcon()).setParent(column1)
				.build();
	}

    private static native Element loginIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 512 512");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "m345.6,305c-28.7,0 -42.5,16 -89.6,16c-47.1,0 -60.8,-16 -89.6,-16"
                          + "c-74.2,0 -134.4,60.2 -134.4,134.4l0,25.6c0,26.5 21.5,48 48,48"
                          + "l352,0c26.5,0 48,-21.5 48,-48l0,-25.6c0,-74.2 -60.2,-134.4 -134.4,-134.4z"
                          + "m86.4,160l-352,0l0,-25.6c0,-47.6 38.8,-86.4 86.4,-86.4c14.6,0 38.3,16 89.6,16"
                          + "c51.7,0 74.9,-16 89.6,-16c47.6,0 86.4,38.8 86.4,86.4l0,25.6zm-176,-176"
                          + "c79.5,0 144,-64.5 144,-144s-64.5,-144 -144,-144s-144,64.5 -144,144"
                          + "s64.5,144 144,144zm0,-240c52.9,0 96,43.1 96,96s-43.1,96 -96,96"
                          + "s-96,-43.1 -96,-96s43.1,-96 96,-96z");
        e.appendChild(p);
        return e;
    }-*/;
	
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
	
}
