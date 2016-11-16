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
 */
public class Fluxxan<State> {

    private final Dispatcher<State> mDispatcher;

    /**
     * Create a new instance
     * @param state The initial state tree
     */
    public Fluxxan(@NonNull State state) {
        mDispatcher = initDispatcher(state);
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
     * Get the dispatcher
     * @return The dispatcher
     */
    public Dispatcher getDispatcher() {
        return mDispatcher;
    }

    /**
     * Inject dispatcher into ActionCreator
     */
    public void inject(ActionCreator ac) {
        ac.setDispatcher(getDispatcher());
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
     * @see Dispatcher#registerMiddleware(Middleware)
     */
    public Middleware<State> registerMiddleware(@NonNull Middleware<State> middleware) {
        return mDispatcher.registerMiddleware(middleware);
    }

    /**
     * @see Dispatcher#unregisterMiddleware(Class)
     */
    public <T extends Middleware<State>> T unregisterMiddleware(Class<T> middlewareClass) {
        return mDispatcher.unregisterMiddleware(middlewareClass);
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

    /**
     * @see Dispatcher#start()
     */
    public void start() {
        mDispatcher.start();
    }

    /**
     * @see Dispatcher#stop()
     */
    public void stop() {
        mDispatcher.stop();
    }
}
