package com.umaplay.fluxxan;

import com.umaplay.fluxxan.impl.DispatcherImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This interface defines a reducer
 * The reducer takes in the State tree and an action {@link Payload} and returns a new State tree based on this
 * Ideally, the reducer should not mutate the state but return a new instance of it if needs to be changed
 * This increases performance by allowing short-circuiting of the notification process
 * @see DispatcherImpl#hasStateChanged(Object, Object)
 * @see StateListener#hasStateChanged(Object, Object)
 * @param <State>
 */
public interface Reducer<State> {
    /**
     * Reduce the action to generate the next state of the app
     * If state is not changed, original state can be returned
     *
     * @param state The current State tree
     * @param payload The action payload
     * @return A {@link com.umaplay.fluxxan.impl.DispatcherImpl.DispatchResult} to indicate if the action was handled and the state
     * @throws Exception
     */
    public DispatcherImpl.DispatchResult<State> reduce(State state, Payload payload) throws Exception;

    /**
     * Check if reducer has been called during current dispatch cycle. Used by {@link Dispatcher} internally.
     * @return True if resolved or False
     */
    public boolean isResolved();

    /**
     * Set the resolved state of the reducer. Used by {@link Dispatcher} internally.
     * @param resolved Whether reducer is resolved or not
     * @return The current reducer instance
     */
    public Reducer<State> setResolved(boolean resolved);

    /**
     * A convenience proxy method for {@link Dispatcher#waitFor(Class, Set, WaitCallback))
     * @param reducer
     * @param callback
     * @throws Exception
     */
    public void waitFor(Class reducer, WaitCallback callback);

    /**
     * A convenience proxy method for {@link Dispatcher#waitFor(Class, Set, WaitCallback))
     * @param reducer
     * @param callback
     * @throws Exception
     */
    public void waitFor(Class[] reducers, WaitCallback callback);

    /**
     * Get the callback passed to {@link #setWaitCallback(WaitCallback)}
     * This is used internally by {@link Dispatcher} to keep track of state of the reducer
     *
     * @return The callback
     */
    public WaitCallback getWaitCallback();

    /**
     * Sets a callback to be retrieved later by {@link #getWaitCallback()}
     * This is used internally by {@link Dispatcher} to keep track of state of the reducer
     *
     * @return The current reducer instance
     */
    public Reducer<State> setWaitCallback(WaitCallback callback);

    /**
     * Gets the list of reducers this reducer is waiting for
     * This is used internally by {@link Dispatcher} to keep track of state of the reducer
     *
     * @return The list of reducer class names
     */
    public List<String> getWaitingOnList();

    /**
     * Adds list of store names to be retrieved later by {@link #getWaitingOnList()}
     * This is used internally by {@link Dispatcher} to keep track of state of the reducer
     *
     * @return The current reducer instance
     */
    public Reducer<State> addToWaitingOnList(Collection<String> reducerNames);

    /**
     * A convenience method to inject the dispatcher into the reducer
     *
     * @param dispatcher The current dispatcher
     * @return The current reducer instance
     */
    public Reducer<State> setDispatcher(Dispatcher dispatcher);

    /**
     * Resets all state flags used by the dispatcher internally
     *
     * @see #setWaitCallback(WaitCallback)
     * @see #addToWaitingOnList(Collection)
     * @see #setResolved(boolean)
     * @return The current reducer instance
     */
    public Reducer<State> reset();
}