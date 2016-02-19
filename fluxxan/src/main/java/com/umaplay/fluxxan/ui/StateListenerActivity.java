package com.umaplay.fluxxan.ui;

import android.support.v7.app.AppCompatActivity;

import com.umaplay.fluxxan.ActionCreator;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.StateListener;

/**
 * Created by user on 6/4/2015.
 */
public abstract class StateListenerActivity<State, ActionCreatorType extends ActionCreator> extends AppCompatActivity implements StateListener<State> {

    protected void onStart() {
        super.onStart();

        getFlux().addListener(this);
        onStateChanged(getFlux().getState());//let's refesh the ui
    }

    protected void onStop() {
        getFlux().removeListener(this);
        super.onStop();
    }

    protected abstract Fluxxan<State, ActionCreatorType> getFlux();

    @Override
    public boolean hasStateChanged(State newState, State oldState) {
        return newState != oldState;
    }

}
