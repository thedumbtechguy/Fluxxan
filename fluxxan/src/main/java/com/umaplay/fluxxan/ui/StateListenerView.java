package com.umaplay.fluxxan.ui;

import android.content.Context;
import android.view.View;

import com.umaplay.fluxxan.ActionCreator;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.StateListener;

/**
 * Created by frostymarvelous on 6/4/2015.
 * This class illustrates the use of Fluxxan in views
 * All you need to do is extend the class or copy the code if you need to extend a View order than View itself like LinearLayout
 */
abstract public class StateListenerView<State> extends View implements StateListener<State> {

    //this mostly should return false
    //and should return true if your view needs to listen to change events even while hidden
    //an example is when you need to listen to change events in order to change the view's visibility
    //else, this should return false to improve performance
    protected boolean mListensWhenHidden;

    protected boolean mIsRegistered;

    public StateListenerView(Context context) {
        super(context);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        registerReducer();
    }

    protected void registerReducer() {
        if(mIsRegistered) return;

        mIsRegistered = true;
        getFlux().addListener(this);
        onStateChanged(getFlux().getState());//let's refesh the ui
    }

    protected void unregisterReducer() {
        mIsRegistered = false;
        getFlux().removeListener(this);
    }

    protected void onDetachedFromWindow() {
        unregisterReducer();
        super.onDetachedFromWindow();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == View.VISIBLE) {
            if(!mIsRegistered) registerReducer();
        }
        else if(!mListensWhenHidden) {
            unregisterReducer();
        }
    }

    protected abstract Fluxxan<State> getFlux();

    @Override
    public boolean hasStateChanged(State newState, State oldState) {
        return newState != oldState;
    }
}
