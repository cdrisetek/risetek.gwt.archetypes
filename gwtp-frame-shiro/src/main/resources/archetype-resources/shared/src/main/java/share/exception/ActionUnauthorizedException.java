package ${package}.share.exception;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.shared.ActionException;

public class ActionUnauthorizedException extends ActionException implements IsSerializable {
	private static final long serialVersionUID = 4538536036410033678L;

	public ActionUnauthorizedException(String message) {
		super(message);
	}
}
