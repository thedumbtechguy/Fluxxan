package com.umaplay.fluxxan.impl;

import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Middleware;
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
 * An abstract implementation of {@link Middleware}
 * This is a good starting point for your middlewares
 *
 * @param <State>
 */
public abstract class BaseMiddleware<State> implements Middleware<State> {

    protected Dispatcher mDispatcher;

    @Override
    public BaseMiddleware<State> setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;

        return this;
    }
}
