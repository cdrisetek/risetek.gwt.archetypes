package ${package}.share.exception;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.shared.ActionException;

public class ActionAuthenticationException extends ActionException implements IsSerializable {
	public ActionAuthenticationException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 7153671763262633857L;

}
