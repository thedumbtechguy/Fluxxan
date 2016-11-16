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
     * @param action The action action
     * @param state The current State tree
     * @throws Exception
     */
    public void before(Action action, State state) throws Exception;

    /**
     * A convenience method to inject the dispatcher into the middleware
     *
     * @param dispatcher The current dispatcher
     * @return The current middleware instance
     */
    public Middleware<State> setDispatcher(Dispatcher dispatcher);
}