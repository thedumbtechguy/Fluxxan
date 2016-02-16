package com.umaplay.fluxxan.impl;

import com.umaplay.fluxxan.Actions;
import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Payload;
import com.umaplay.fluxxan.Reducer;

/**
 * Created by user on 5/8/2015.
 */
public class BaseActions implements Actions {
    protected Dispatcher mDispatcher;

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }


    public void dispatch(Payload payload) {
        mDispatcher.dispatch(payload);
    }
}
