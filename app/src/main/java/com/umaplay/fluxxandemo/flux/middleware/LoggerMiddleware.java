package com.umaplay.fluxxandemo.flux.middleware;

import android.util.Log;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseMiddleware;
import com.umaplay.fluxxandemo.flux.model.AppState;

/**
 * Created by niltonvasques on 11/15/16.
 */

public class LoggerMiddleware extends BaseMiddleware<AppState> {
    @Override
    public void intercept(AppState appState, Action action) throws Exception {
        Log.d("[LoggerMiddleware", action.Type);
    }
}
