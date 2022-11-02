package ${package}.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.dispatch.GetResult;
import ${package}.share.dispatch.GetResults;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction;
import ${package}.share.dispatch.UnsecuredSerializableBatchAction.OnException;

public class SerializableBatchActionHandler implements ActionHandler<UnsecuredSerializableBatchAction,
		GetResults<GetResult<? extends IsSerializable>>> {

    public GetResults<GetResult<?>> execute(UnsecuredSerializableBatchAction action, ExecutionContext context)
            throws ActionException {

        OnException onException = action.getOnException();

        List<GetResult<?>> results = new ArrayList<>();

        for (Action<?> a : action.getActions()) {
            try {
                results.add(new GetResult<IsSerializable>((IsSerializable) context.execute(a)));
            } catch (Exception e) {
                if (onException == OnException.ROLLBACK) {
                    if (e instanceof ActionException) {
                        throw (ActionException) e;
                    }
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new ActionException(e);
                    }
                }
            }
        }
        return new GetResults<>(results);
    }

    
	@Override
	public void undo(UnsecuredSerializableBatchAction action, GetResults<GetResult<? extends IsSerializable>> result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<UnsecuredSerializableBatchAction> getActionType() {
		return UnsecuredSerializableBatchAction.class;
	}
}