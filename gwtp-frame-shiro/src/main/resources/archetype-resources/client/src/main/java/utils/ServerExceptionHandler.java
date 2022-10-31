package ${package}.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import ${package}.NameTokens;
import ${package}.place.error.RevealErrorEventHandler.RevealErrorEvent;
import ${package}.share.exception.ActionAuthenticationException;
import ${package}.share.exception.ActionUnauthenticatedException;
import ${package}.share.exception.ActionUnauthorizedException;
import ${package}.share.exception.ActionUninitializedException;

@Singleton
public class ServerExceptionHandler {
	
	private final PlaceManager placeManager;
	private final EventBus eventBus;
	@Inject
	public ServerExceptionHandler(final PlaceManager placeManager, final EventBus eventBus) {
		this.placeManager = placeManager;
		this.eventBus = eventBus;
	}

	public void handler(Throwable caught) {
		if(caught instanceof ServiceException) {
			ServiceException e = (ServiceException)caught;
			placeManager.revealErrorPlace(NameTokens.error);
			GWT.log("Server Exception:\r\n" + e.getMessage());
		} else if(caught instanceof StatusCodeException) {
			StatusCodeException e = (StatusCodeException)caught;
			if(e.getStatusCode() == 500) { // http Server Error.
				GWT.log("Server Error, Panic");
				eventBus.fireEvent(new RevealErrorEvent("Server Error, Panic"));
//				placeManager.revealErrorPlace(NameTokens.error);
			}
		}
		else if(caught instanceof ActionUnauthorizedException) {
			GWT.log("Action Unauthorized");
			placeManager.revealUnauthorizedPlace(null);
		}
		else if(caught instanceof ActionAuthenticationException) {
			GWT.log("Action AuthenticationException");
			placeManager.revealUnauthorizedPlace(null);
		}
		else if(caught instanceof ActionUnauthenticatedException) {
			GWT.log("Action ActionUnauthenticatedException: " + caught.getMessage());
			// TODO: GO to UnauthorizedPlace not login place?
			placeManager.revealUnauthorizedPlace(null);
		} else if(caught instanceof ActionException) {
			GWT.log(" caught instanceof ActionException");
			GWT.log("Server State Failed." + caught.getClass());
			eventBus.fireEvent(new RevealErrorEvent("ActionException"));
		} else if(caught instanceof ActionUninitializedException) {
			GWT.log(" caught instanceof ActionUninitializedException");
			GWT.log("Server State Failed." + caught.getClass());
			eventBus.fireEvent(new RevealErrorEvent("ActionUninitializedException"));
		} else {
			GWT.log("Server State Failed." + caught);
			GWT.log("Server State Failed." + caught.getClass());
			GWT.log("Server State Failed." + caught.getCause().getClass());
		}
	}
}
