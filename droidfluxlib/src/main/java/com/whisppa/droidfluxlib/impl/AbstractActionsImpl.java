package com.whisppa.droidfluxlib.impl;

import com.whisppa.droidfluxlib.Actions;
import com.whisppa.droidfluxlib.Dispatcher;

/**
 * Created by user on 5/8/2015.
 */
public class AbstractActionsImpl implements Actions {
    protected Dispatcher mDispatcher;

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }
}
