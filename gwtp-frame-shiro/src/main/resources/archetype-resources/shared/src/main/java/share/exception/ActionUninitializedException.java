package ${package}.share.exception;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.shared.ActionException;

public class ActionUninitializedException extends ActionException implements IsSerializable {
	private static final long serialVersionUID = -6907462023652812532L;

	public ActionUninitializedException(String message) {
		super(message);
	}
}
