package ${package}.share.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.Result;

@SuppressWarnings("serial")
public class GetResult<T extends IsSerializable> implements Result {
	T result;

    protected GetResult() {}

    public GetResult(T results) {
        this.result = results;
    }

    public T getResults() {
        return result;
    }
}
