package ${package}.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import ${package}.NameTokens;
import ${package}.share.exception.ActionAuthenticationException;
import ${package}.share.exception.ActionUnauthenticatedException;
import ${package}.share.exception.ActionUnauthorizedException;

public class ServerExceptionHandler {
	
	private static final PlaceRequest loginPlaceRequest = new PlaceRequest.Builder().nameToken(NameTokens.login).build();

	
	public static void handler(PlaceManager placeManager, Throwable caught) {
		if(caught instanceof ServiceException) {
			ServiceException e = (ServiceException)caught;
			GWT.log("Server Exception:\r\n" + e.getMessage());
		} else if(caught instanceof StatusCodeException) {
			StatusCodeException e = (StatusCodeException)caught;
			if(e.getStatusCode() == 500) { // http Server Error.
				GWT.log("Server Error, Panic");
			}
		}
		else if(caught instanceof ActionUnauthorizedException) {
			GWT.log("Action Unauthorized");
			// TODO: goto login.
		}
		else if(caught instanceof ActionAuthenticationException) {
			GWT.log("Action AuthenticationException");
		}
		else if(caught instanceof ActionUnauthenticatedException) {
			if(null != placeManager) {
				placeManager.revealPlace(loginPlaceRequest);
			}
		}
		else if(caught instanceof ActionException) {
			GWT.log(" caught instanceof ActionException");
			GWT.log("Server State Failed." + caught.getClass());
		} else {
			GWT.log("Server State Failed." + caught);
			GWT.log("Server State Failed." + caught.getClass());
			GWT.log("Server State Failed." + caught.getCause().getClass());
		}
	}
}
