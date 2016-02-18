package com.umaplay.fluxxan.ui;

import android.support.v4.app.Fragment;

import com.umaplay.fluxxan.Flux;
import com.umaplay.fluxxan.StateListener;

/**
 * Created by user on 6/4/2015.
 */
abstract public class StateListenerFragment<State> extends Fragment implements StateListener<State> {

     public void onStart() {
        super.onStart();
        getFlux().addListener(this);
     }

    public void onStop() {
        getFlux().removeListener(this);
        super.onStop();
    }

    protected abstract Flux getFlux();
}
