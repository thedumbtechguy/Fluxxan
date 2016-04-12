package com.umaplay.fluxxan.ui;

import android.support.v4.app.Fragment;

import com.umaplay.fluxxan.ActionCreator;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.StateListener;

/**
 * Created by user on 6/4/2015.
 */
abstract public class StateListenerFragment<State> extends Fragment implements StateListener<State> {

     public void onStart() {
        super.onStart();

        getFlux().addListener(this);
        onStateChanged(getFlux().getState());//let's refesh the ui
     }

    public void onStop() {
        getFlux().removeListener(this);
        super.onStop();
    }

    protected abstract Fluxxan<State> getFlux();

    @Override
    public boolean hasStateChanged(State newState, State oldState) {
        return newState != oldState;
    }
}
