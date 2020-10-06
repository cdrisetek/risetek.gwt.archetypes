package ${package}.share;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class UnsecuredSerializableBatchAction extends UnsecuredActionImpl<GetResults<GetResult<? extends IsSerializable>>> {
    /**
     * {@link UnsecuredSerializableBatchAction}'s enumeration of possible outcomes when there is an exception.
     */
    public enum OnException implements IsSerializable {
        /**
         * If specified, the batch will continue if an action fails. The matching {@link GetResult} in the
         * {@link GetResults#getResults()} will be <code>null</code>.
         */
        CONTINUE,
        /**
         * If specified, the batch will stop processing and roll back any executed actions from the batch, and throw the
         * exception.
         */
        ROLLBACK
    }

    private Action<?>[] actions;

    private OnException onException;

    /**
     * Constructs a new batch action, which will attempt to execute the provided list of actions in order. If there is a
     * failure, it will follow the rules specified by <code>onException</code>.
     *
     * @param onException If there is an exception, specify the behaviour.
     * @param actions     The list of actions to execute.
     */
    public UnsecuredSerializableBatchAction(OnException onException, Action<?>... actions) {
        this.onException = onException;
        this.actions = actions;
    }

    /**
     * Used for serialization only.
     */
    protected UnsecuredSerializableBatchAction() {
    }

    /**
     * The list of actions to execute.
     *
     * @return The actions.
     */
    public Action<?>[] getActions() {
        return actions;
    }

    /**
     * The expected behaviour if any of the sub-actions throw an exception.
     *
     * @return The exception handling behaviour.
     */
    public OnException getOnException() {
        return onException;
    }
} 
