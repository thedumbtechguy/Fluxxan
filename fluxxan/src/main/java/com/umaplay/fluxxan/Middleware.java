package com.umaplay.fluxxan;

import com.umaplay.fluxxan.impl.DispatcherImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static android.R.attr.action;

/**
 * This interface defines a middleware
 * The middleware intercept the action {@link Action} been dispatched through the Fluxxan and
 * the current state.
 * @param <State>
 */
public interface Middleware<State> {
    /**
     * Intercept the action to been dispatched
     *
     * @param state The current State tree
     * @param action The action action
     * @return A {@link DispatchResult} to indicate if the action was handled and the state
     * @throws Exception
     */
    public void intercept(State state, Action action) throws Exception;

    /**
     * A convenience method to inject the dispatcher into the reducer
     *
     * @param dispatcher The current dispatcher
     * @return The current reducer instance
     */
    public Middleware<State> setDispatcher(Dispatcher dispatcher);

    /**
     * Resets all state flags used by the dispatcher internally
     *
     * @return The current reducer instance
     */
    //public Middleware<State> reset();
}