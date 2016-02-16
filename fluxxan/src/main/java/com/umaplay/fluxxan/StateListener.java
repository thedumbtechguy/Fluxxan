package com.umaplay.fluxxan;

/**
 * Defines an object that listens to changes to the state.
 *
 * @param <State>
 */
public interface StateListener<State> {
    /**
     * Should determine if the relevant branch of the state tree this listener depends on has changed
     * If False, StateListener#onStateChanged is not called
     *
     * @param newState The new state
     * @param oldState The old state
     * @return True if state has changed else False
     */
    public boolean hasStateChanged(State newState, State oldState);

    /**
     * Called when state has changed
     *
     * @param state The entire state tree
     */
    public void onStateChanged(final State state);
}
