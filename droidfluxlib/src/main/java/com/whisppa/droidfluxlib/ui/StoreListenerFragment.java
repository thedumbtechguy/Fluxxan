package com.whisppa.droidfluxlib.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;
import com.whisppa.droidfluxlib.utils.StoreHelper;

/**
 * Created by user on 6/4/2015.
 */
abstract public class StoreListenerFragment extends Fragment implements StoreListener {

    protected StoreHelper mStoreHelper = new StoreHelper(this, getFlux(), getStores());

    public void onStart() {
        super.onStart();
        mStoreHelper.onStart();
    }

    public void onStop() {
        mStoreHelper.onStop();
        super.onStop();
    }

    protected abstract Class[] getStores();
    protected abstract Flux getFlux();
}
