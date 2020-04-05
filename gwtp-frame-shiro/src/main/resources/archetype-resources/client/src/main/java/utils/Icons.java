package ${package}.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class Icons {
	public static class ArrowRight extends Icon {
		public ArrowRight() {
			super("0 0 24 24");
			createPath("M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z");
		}
	}

	public static class ArrowLeft extends Icon {
		public ArrowLeft() {
			super("0 0 24 24");
			createPath("m12,4l1.41,1.41l-5.58,5.59l12.17,0l0,2l-12.17,0l5.58,5.59l-1.41,1.41l-8,-8l8,-8z");
		}
	}

	public static class Login extends Icon {
		public Login() {
			super("0 0 512 512");
			createPath("m345.6,305c-28.7,0 -42.5,16 -89.6,16c-47.1,0 -60.8,-16 -89.6,-16"
                    + "c-74.2,0 -134.4,60.2 -134.4,134.4l0,25.6c0,26.5 21.5,48 48,48"
                    + "l352,0c26.5,0 48,-21.5 48,-48l0,-25.6c0,-74.2 -60.2,-134.4 -134.4,-134.4z"
                    + "m86.4,160l-352,0l0,-25.6c0,-47.6 38.8,-86.4 86.4,-86.4c14.6,0 38.3,16 89.6,16"
                    + "c51.7,0 74.9,-16 89.6,-16c47.6,0 86.4,38.8 86.4,86.4l0,25.6zm-176,-176"
                    + "c79.5,0 144,-64.5 144,-144s-64.5,-144 -144,-144s-144,64.5 -144,144"
                    + "s64.5,144 144,144zm0,-240c52.9,0 96,43.1 96,96s-43.1,96 -96,96"
                    + "s-96,-43.1 -96,-96s43.1,-96 96,-96z");
		}
	}
	
	public static class Modify extends Icon {
		public Modify() {
			super("0 0 576 512");
			createPath("M402.3 344.9l32-32c5-5 13.7-1.5 13.7 5.7V464c0 26.5-21.5 "
					 + "48-48 48H48c-26.5 0-48-21.5-48-48V112c0-26.5 21.5-48 "
					 + "48-48h273.5c7.1 0 10.7 8.6 5.7 13.7l-32 32c-1.5 1.5-3.5 "
					 + "2.3-5.7 2.3H48v352h352V350.5c0-2.1.8-4.1 2.3-5.6zm156.6-201.8L296.3 "
					 + "405.7l-90.4 10c-26.2 2.9-48.5-19.2-45.6-45.6l10-90.4L432.9 "
					 + "17.1c22.9-22.9 59.9-22.9 82.7 0l43.2 43.2c22.9 22.9 22.9 60 "
					 + ".1 82.8zM460.1 174L402 115.9 216.2 301.8l-7.3 65.3 65.3-7.3L460.1 "
					 + "174zm64.8-79.7l-43.2-43.2c-4.1-4.1-10.8-4.1-14.8 0L436 "
					 + "82l58.1 58.1 30.9-30.9c4-4.2 4-10.8-.1-14.9z");
		}
	}
	
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

    public static native Element eyeSlashIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M10.58,7.25l1.56,1.56c1.38,0.07,2.47,1.17,2.54,2.54"
                          + "l1.56,1.56C16.4,12.47,16.5,12,16.5,11.5"
                          + "C16.5,9.02,14.48,7,12,7 C11.5,7,11.03,7.1,10.58,7.25z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12,6c3.79,0,7.17,2.13,8.82,5.5c-0.64,1.32-1.56,2.44-2.66,3.33"
                          + "l1.42,1.42c1.51-1.26,2.7-2.89,3.43-4.74 "
                          + "C21.27,7.11,17,4,12,4c-1.4,0-2.73,0.25-3.98,0.7"
                          + "L9.63,6.3C10.4,6.12,11.19,6,12,6z");
        e.appendChild(p);

		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M16.43,15.93l-1.25-1.25l-1.27-1.27l-3.82-3.82L8.82,8.32"
                          + "L7.57,7.07L6.09,5.59L3.31,2.81L1.89,4.22l2.53,2.53 "
                          + "C2.92,8.02,1.73,9.64,1,11.5C2.73,15.89,7,19,12,19"
                          + "c1.4,0,2.73-0.25,3.98-0.7l4.3,4.3l1.41-1.41l-3.78-3.78"
                          + "L16.43,15.93z M11.86,14.19c-1.38-0.07-2.47-1.17-2.54-2.54"
                          + "L11.86,14.19z M12,17c-3.79,0-7.17-2.13-8.82-5.5"
                          + "c0.64-1.32,1.56-2.44,2.66-3.33 l1.91,1.91"
                          + "C7.6,10.53,7.5,11,7.5,11.5"
                          + "c0,2.48,2.02,4.5,4.5,4.5c0.5,0,0.97-0.1,1.42-0.25"
                          + "l0.95,0.95C13.6,16.88,12.81,17,12,17z");
        e.appendChild(p);

        return e;
    }-*/;

    public static native Element eyeIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 24 24");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12,7c-2.48,0-4.5,2.02-4.5,4.5S9.52,16,12,16"
                          + "s4.5-2.02,4.5-4.5S14.48,7,12,7z M12,14.2"
                          + "c-1.49,0-2.7-1.21-2.7-2.7 c0-1.49,1.21-2.7,2.7-2.7"
                          + "s2.7,1.21,2.7,2.7C14.7,12.99,13.49,14.2,12,14.2z");
        e.appendChild(p);

   		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M12,4C7,4,2.73,7.11,1,11.5C2.73,15.89,7,19,12,19"
                          + "s9.27-3.11,11-7.5C21.27,7.11,17,4,12,4z M12,17 "
                          + "c-3.79,0-7.17-2.13-8.82-5.5C4.83,8.13,8.21,6,12,6"
                          + "s7.17,2.13,8.82,5.5C19.17,14.87,15.79,17,12,17z");
        e.appendChild(p);

   		p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M0,0h24v24H0V0z");
		p.setAttribute("fill", "none");
        e.appendChild(p);

        return e;
    }-*/;

    public static native Element compassIcon()
    /*-{
        var ns = "http://www.w3.org/2000/svg";
        var e = document.createElementNS(ns, "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox","0 0 496 512");
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
		var p = document.createElementNS(ns, "path");
        p.setAttribute("d", "M347.94 129.86L203.6 195.83a31.938 31.938 0 0 0-15.77"
                          + " 15.77l-65.97 144.34c-7.61 16.65 9.54 33.81 26.2 26.2l144.34-65.97a31.938"
                          + " 31.938 0 0 0 15.77-15.77l65.97-144.34c7.61-16.66-9.54-33.81-26.2-26.2zm-77.36 148.72c-12.47"
                          + " 12.47-32.69 12.47-45.16 0-12.47-12.47-12.47-32.69 0-45.16 12.47-12.47 32.69-12.47"
                          + " 45.16 0 12.47 12.47 12.47 32.69 0 45.16zM248 8C111.03 8 0 119.03 0 256s111.03"
                          + " 248 248 248 248-111.03 248-248S384.97 8 248 8zm0 448c-110.28 0-200-89.72-200-200S137.72"
                          + " 56 248 56s200 89.72 200 200-89.72 200-200 200z");
        e.appendChild(p);


        return e;
    }-*/;


    public static native Element elementNS(String view) /*-{
        var e = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    	e.setAttribute("fill-rule", "evenodd");
    	e.setAttribute("viewBox", view);
    	e.setAttribute("preserveAspectRatio","xMidYMid meet");
    	e.setAttribute("focusable","false");
        return e;
    }-*/;
	
    public static native Element elementNSPath() /*-{
        return document.createElementNS("http://www.w3.org/2000/svg", "path");
    }-*/;

    public static class Icon extends Widget {
		public Icon(String view) {
			setElement(elementNS(view));
		}
		
		public Element createPath(String path) {
	        Element p = elementNSPath();
	        p.setAttribute("d", path);
	        getElement().appendChild(p);
	        return p;
		}
		
		public Element setFillNone(Element e) {
			e.setAttribute("fill", "none");
			return e;
		}
/*		
		public Element setOpacity(String opacity) {
			
		}
*/
	}
}
