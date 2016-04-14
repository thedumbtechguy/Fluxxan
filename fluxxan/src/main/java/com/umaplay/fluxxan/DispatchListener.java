package com.umaplay.fluxxan;

/**
 * Defines an object that listens to changes to the state.
 *
 * @param <State>
 */
public interface DispatchListener<State> {
    /**
     * Called before an action is dispatched.
     *
     * @param action The action to be dispatched
     * @param currentState The current state after the dispatch.
     */
    public void beforeDispatch(Action action, State currentState);

    /**
     * Called when an exception occurs during a dispatch.
     *
     * @param action The action that caused the Exception
     * @param throwable The throwable.
     */
    public void onDispatchException(Action action, Exception ex);

    /**
     * Called after the dispatch is complete.
     *
     * @param action The action that was dispatched
     * @param currentState The current state after the dispatch.
     * @param stateChanged Whether or not the state was changed
     * @param wasHandled Whether or not the action was handled
     */
    public void afterDispatch(Action action, State currentState, Boolean stateChanged, Boolean
            wasHandled);

}
