package ${package}.server;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.exception.ActionAuthenticationException;
import ${package}.share.exception.ActionUnauthenticatedException;
import ${package}.share.exception.ActionUnauthorizedException;

/**
 * Map Server side Shiro Exception to common Exception, so GWT Client may handler exception.
 * @author wangyc@risetek.com
 *
 */
public class ActionExceptionMapper {
	public static void handler(Throwable t) throws ActionException {
		if(t instanceof AuthenticationException) {
			throw new ActionAuthenticationException();
		}
		else if(t instanceof UnauthenticatedException) {
			throw new ActionUnauthenticatedException("UnauthenticatedException");
		}
		else if(t instanceof UnauthorizedException) {
				System.out.println("\r\n\r\n\r\n !!!!!\r\n" + t.getMessage() + "\r\n\r\n!!!!\r\n");
				if(t.getCause() != null) {
				System.out.println(t.getCause());
				System.out.println(t.getCause().getMessage());
				}
			throw new ActionUnauthorizedException();
		}
		if(t instanceof ActionException) {
			// forward
			throw (ActionException)t;
		}
		else {
			throw new ActionException("TODO: UnMapped exception:\r\n  class: " + t + "\r\n" + t.getMessage());
		}
	}
}
