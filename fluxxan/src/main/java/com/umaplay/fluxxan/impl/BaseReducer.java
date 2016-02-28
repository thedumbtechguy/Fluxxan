package com.umaplay.fluxxan.impl;

import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Reducer;
import com.umaplay.fluxxan.WaitCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract implementation of {@link Reducer}
 * This is a good starting point for your reducers
 *
 * @param <State>
 */
public abstract class BaseReducer<State> implements Reducer<State> {

    protected Dispatcher mDispatcher;
    protected boolean mIsResolved = false;
    protected WaitCallback mWaitCallback = null;
    protected final List<String> mWaitingOnList;

    //ensure you call super else!!!
    public BaseReducer() {
        mWaitingOnList = Collections.synchronizedList(new ArrayList<String>());
    }

    @Override
    public BaseReducer<State> setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;

        return this;
    }


    @Override
    public BaseReducer<State> reset() {
        mIsResolved = false;
        mWaitingOnList.clear();
        mWaitCallback = null;

        return this;
    }

    @Override
    public List<String> getWaitingOnList() {
        return mWaitingOnList;
    }

    @Override
    public WaitCallback getWaitCallback() {
        return mWaitCallback;
    }

    @Override
    public BaseReducer<State> setWaitCallback(WaitCallback callback) {
        mWaitCallback = callback;

        return this;
    }


    @Override
    public boolean isResolved() {
        return mIsResolved;
    }

    @Override
    public BaseReducer<State> setResolved(boolean resolved) {
        mIsResolved = resolved;

        return this;
    }

    @Override
    public BaseReducer<State> addToWaitingOnList(Collection<String> reducerNames) {
        mWaitingOnList.addAll(reducerNames);

        return this;
    }


    @Override
    public void waitFor(Class[] reducers, WaitCallback callback) {
        Set<Class> _reducers = new HashSet<>(Arrays.asList(reducers));

        mDispatcher.waitFor(this.getClass(), _reducers, callback);
    }

    @Override
    public void waitFor(Class reducer, WaitCallback callback) {
        waitFor(new Class[]{reducer}, callback);
    }

}
