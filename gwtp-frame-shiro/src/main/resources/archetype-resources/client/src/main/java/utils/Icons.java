package com.risetek.utils;

import com.google.gwt.dom.client.Element;

public class Icons {
    public static native Element arrowIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z");
        e.appendChild(p);
        return e;
    }-*/;

	
    public static native Element loginIcon()
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
    
	
    public static native Element matIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
    	
        var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z");
        e.appendChild(p);
        return e;
    }-*/;

    public static native Element helpIcon()
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
	
    public static native Element serverIcon()
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
    
    public static native Element aboutIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M5.336 19.49a3.556 3.556 0 1 1-.002-7.112 3.556 3.556 0 0 1 .002 7.112z"
                          + "m4.365-.486c.6-.86.95-1.9.95-3.022a5.32 5.32 0 0 0-5.32-5.315 5.32"
                          + " 5.32 0 0 0-4.68 7.85 5.34 5.34 0 0 0 2.375 2.257c.698.336 1.48.524"
                          + " 2.307.524a5.3 5.3 0 0 0 3.02-.938l3.51 3.523a.4.4 0 0 0 .564.002"
                          + "l.8-.79a.398.398 0 0 0 0-.564L9.7 19.01z");
        p.setAttribute("opacity", ".8");
        p.setAttribute("fill-rule", "nonzero");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M11.76 17.493c.17-.573.263-1.18.263-1.806 0-3.59-3.023-6.502-6.752-6.502"
                           + "a6.95 6.95 0 0 0-2.3.39V4.852a1 1 0 0 1 1-1h18.92a1 1 0 0 1 1 1v11.64"
                           + "a1 1 0 0 1-1 1H11.76z");
        p.setAttribute("fill-rule", "nonzero");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16.296 3.852V2.074h-5.333V3.21l-1.778.642V1.296a1 1 0 0 1 1-1"
                          + "h6.89a1 1 0 0 1 1 1v2l-1.768.556h-.01z");
        p.setAttribute("opacity", ".8");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16.296 3.86l1.78-.57v.57zm-7.128-.005l1.786-.645v.645z");
        p.setAttribute("opacity", ".6");
        e.appendChild(p);
        return e;
    }-*/;
    
    public static native Element convertIcon()
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
    

    public static native Element homeIcon()
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
