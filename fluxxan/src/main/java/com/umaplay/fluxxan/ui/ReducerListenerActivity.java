package com.umaplay.fluxxan.ui;

import android.support.v7.app.AppCompatActivity;

import com.umaplay.fluxxan.Flux;
import com.umaplay.fluxxan.StateListener;

/**
 * Created by user on 6/4/2015.
 */
abstract public class ReducerListenerActivity<State> extends AppCompatActivity implements StateListener<State> {

    protected void onStart() {
        super.onStart();
        getFlux().addListener(this);
    }

    protected void onStop() {
        getFlux().removeListener(this);
        super.onStop();
    }

    protected abstract Flux getFlux();

}
