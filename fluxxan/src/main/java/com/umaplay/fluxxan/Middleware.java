package com.umaplay.fluxxan;

/**
 * This interface defines a middleware
 * The middleware intercepts the action {@link Action} before been dispatched through the Fluxxan
 * and the current state.
 * @param <State>
 */
public interface Middleware<State> {
    /**
     * Intercept the action to been dispatched
     *
     * @param state The current State tree
     * @param action The action action
     * @throws Exception
     */
    public void intercept(State state, Action action) throws Exception;

    /**
     * A convenience method to inject the dispatcher into the middleware
     *
     * @param dispatcher The current dispatcher
     * @return The current middleware instance
     */
    public Middleware<State> setDispatcher(Dispatcher dispatcher);
}