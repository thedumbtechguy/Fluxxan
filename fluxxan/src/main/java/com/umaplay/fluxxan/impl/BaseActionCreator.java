package com.umaplay.fluxxan.impl;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.ActionCreator;
import com.umaplay.fluxxan.Dispatcher;

/**
 * Created by user on 5/8/2015.
 */
public class BaseActionCreator implements ActionCreator {
    protected Dispatcher mDispatcher;

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }


    public void dispatch(Action action) {
        mDispatcher.dispatch(action);
    }
}
