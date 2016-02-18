package com.umaplay.fluxxan;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.umaplay.fluxxan.impl.DispatcherImpl;

import java.util.Collection;
import java.util.List;

/**
 * This class serves as a coordinating object
 * It's only job is to instantiate the dispatcher.
 * It contains a bunch of proxy methods that call the corresponding methods on the dispatcher
 * It is not essential to use Fluxxan but helps quite a bit in keeping things cohesive.
 * By default, we use the {@link DispatcherImpl} provided. This can be changed by overriding {@link #initDispatcher(Object)}
 *
 * @param <State>
 * @param <ActionCreatorType>
 */
public class Flux<State, ActionCreatorType extends ActionCreator> {

    private final Dispatcher<State> mDispatcher;
    private final ActionCreatorType mActionCreator;

    /**
     * Create a new instance
     * @param state The initial state tree
     */
    public Flux(@NonNull State state) {
        mDispatcher = initDispatcher(state);
        mActionCreator = null;
    }

    /**
     * Create a new instance with optional ActionCreator
     * ActionCreator is injected with the dispatcher instance
     *
     * @param state The initial state tree
     * @param actionCreator The actions that will be returned by {@link #getActionCreator()}
     */
    public Flux(@NonNull State state, ActionCreatorType actionCreator) {
        mDispatcher = initDispatcher(state);

        actionCreator.setDispatcher(mDispatcher);
        mActionCreator = actionCreator;
    }

    /**
     * This can be overridden to provide a different {@link Dispatcher}
     * This is called once.
     *
     * @param state The intial state tree
     * @return An instance of the {@link Dispatcher}
     */
    protected Dispatcher<State> initDispatcher(State state) {
        return new DispatcherImpl<>(state);
    }

    /**
     * Get the actions passed in {@link #Flux(Object, com.umaplay.fluxxan.ActionCreator)}
     * @return ActionCreatorType object or null if not provided
     */
    public @Nullable
    ActionCreatorType getActionCreator() {
        return mActionCreator;
    }

    /**
     * Get the dispatcher
     * @return The dispatcher
     */
    public Dispatcher getDispatcher() {
        return mDispatcher;
    }


    //dispatcher proxy methods

    /**
     * @see Dispatcher#getState()
     */
    public State getState() {
        return mDispatcher.getState();
    }

    /**
     * @see Dispatcher#getReducer(Class)
     */
    public <T extends Reducer<State>> T getReducer(Class<T> reducerClass) {
        return mDispatcher.getReducer(reducerClass);
    }

    /**
     * @see Dispatcher#registerReducer(Reducer)
     */
    public Reducer<State> registerReducer(@NonNull Reducer<State> reducer) {
        return mDispatcher.registerReducer(reducer);
    }

    /**
     * @see Dispatcher#registerReducers(List)
     */
    protected Collection<Reducer<State>> registerReducers(@NonNull List<Reducer<State>> reducers) {
        return mDispatcher.registerReducers(reducers);
    }

    /**
     * @see Dispatcher#unregisterReducer(Class)
     */
    public <T extends Reducer<State>> T unregisterReducer(Class<T> reducer) {
        return mDispatcher.unregisterReducer(reducer);
    }

    /**
     * @see Dispatcher#addListener(StateListener)
     */
    public boolean addListener(StateListener<State> stateListener) {
        return mDispatcher.addListener(stateListener);
    }

    /**
     * @see Dispatcher#removeListener(StateListener)
     */
    public boolean removeListener(StateListener<State> stateListener) {
        return mDispatcher.removeListener(stateListener);
    }
}
