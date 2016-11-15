package com.umaplay.fluxxan;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The dispatcher.
 * This handles dispatching of all actions and notifying of registered listeners of changes
 *
 * @param <State> Your state object type. Ideally an immutable object
 */
public interface Dispatcher<State> {
    /**
     * Start the dispatcher.
     */
    public void start();

    /**
     * Stop the dispatcher.
     */
    public void stop();

    /**
     * Dispatch an action
     * @param action An action object containing the action type and the data action
     */
    public void dispatch(@NonNull Action action);

    /**
     * Get the current state
     * @return The current state
     */
    public State getState();

    /**
     * Get the reducer of the given class if registered
     *
     * @param reducerClass Class of reducer to return
     * @param <T> The class type of the reducer you wish to return. Should implement {@link Reducer}
     * @return The reducer or null
     */
    public @Nullable <T extends Reducer<State>> T getReducer(Class<T> reducerClass);

    /**
     * Get all registered reducers
     *
     * @return A collection of all registered reducers
     */
    public Collection<Reducer<State>> getReducers();

    /**
     * Register a reducer
     *
     * @param reducer Reducer to be registered
     * @return The reducer
     */
    public Reducer<State> registerReducer(@NonNull Reducer<State> reducer);

    /**
     * Register a list of reducers
     * @param reducers The list of reducers to be registered
     * @return The list of reducers
     */
    public Collection<Reducer<State>> registerReducers(@NonNull List<Reducer<State>> reducers);

    /**
     * Unregister a reducer
     *
     * @param reducerClass Class of reducer you wish to unregister
     * @param <T> The class type of the reducer you wish to unregister.
     * @return The unregistered reducer or null if not registered
     */
    public @Nullable <T extends Reducer<State>> T unregisterReducer(Class<T> reducerClass);

    /**
     * Register a middleware
     *
     * @param middleware Middleware to be registered
     * @return The middleware
     */
    public Middleware<State> registerMiddleware(@NonNull Middleware<State> middleware);

    /**
     * Unregister a middleware
     *
     * @param middlewareClass Class of middleware you wish to unregister.
     * @return The middleware
     */
    public @Nullable <T extends Middleware<State>> T unregisterMiddleware(Class<T> middlewareClass);

    /**
     * Add a listener to listen to state changes
     * @param StateListener The listener
     * @return True on success or False on failure
     */
    public boolean addListener(StateListener<State> StateListener);

    /**
     * Remove a listener
     * @param StateListener The listener
     * @return True on success or False on failure
     */
    public boolean removeListener(StateListener<State> StateListener);

    /**
     * Wait for a given reducer before being applied to the state.
     * This should be called a reducer when it is being applied.
     *
     * @param waitingReducer The reducer that is waiting
     * @param reducers A set of the Classes of the reducers to wait for
     * @param callback Callback to be called after specified reducers have been applied
     */
    public void waitFor(Class waitingReducer, Set<Class> reducers, WaitCallback callback);

    /**
     * Check if dispatcher is currently dispatching
     *
     * @return True if dispatching or False
     */
    public boolean isDispatching();

    /**
     * Can be overridden to short-circuit the notification of listeners if the state has not changes
     * Especially useful when using an Immutable object to hold state. Check can simply be `newState != oldState`.
     *
     * @param newState The new state
     * @param oldState The old state
     * @return True if state has changed or False
     */
    public boolean hasStateChanged(State newState, State oldState);
}
