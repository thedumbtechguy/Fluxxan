package com.whisppa.droidfluxlib.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;
import com.whisppa.droidfluxlib.utils.StoreHelper;

/**
 * Created by user on 6/4/2015.
 */
abstract public class StoreListenerActivity extends AppCompatActivity implements StoreListener {

    protected StoreHelper mStoreHelper  = new StoreHelper(this, getFlux(), getStores());

    protected void onStart() {
        super.onStart();
        mStoreHelper.onStart();
    }

    protected void onStop() {
        mStoreHelper.onStop();
        super.onStop();
    }

    protected abstract Class[] getStores();
    protected abstract Flux getFlux();

}
