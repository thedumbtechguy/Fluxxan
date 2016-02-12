package com.whisppa.droidfluxlib.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;
import com.whisppa.droidfluxlib.utils.StoreHelper;

/**
 * Created by frostymarvelous on 6/4/2015.
 * This class illustrates the use of Flux in views
 * All you need to do is extend the class or copy the code if you need to extend a View order than View itself like LinearLayout
 */
abstract public class StoreListenerView extends View implements StoreListener {

    protected StoreHelper mStoreHelper = new StoreHelper(this, getFlux(), getStores());

    protected boolean mIsRegistered;

    public StoreListenerView(Context context) {
        super(context);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        registerStore();
    }

    protected void registerStore() {
        if(mIsRegistered) return;

        mIsRegistered = true;
        mStoreHelper.onStart();
    }

    protected void unregisterStore() {
        mIsRegistered = false;
        mStoreHelper.onStop();
    }

    protected void onDetachedFromWindow() {
        unregisterStore();
        super.onDetachedFromWindow();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == View.VISIBLE) {
            if(!mIsRegistered) registerStore();
        }
        else if(!listensToStoreWhenHidden()) {
            unregisterStore();
        }
    }

    //this mostly should return false
    //and should return true if your view needs to listen to change events even while hidden
    //an example is when you need to listen to change events in order to change the view's visibility
    //else, this should return false to improve performance
    protected abstract boolean listensToStoreWhenHidden();
    protected abstract Class[] getStores();
    protected abstract Flux getFlux();
}
