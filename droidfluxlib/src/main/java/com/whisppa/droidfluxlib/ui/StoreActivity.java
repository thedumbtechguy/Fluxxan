package com.whisppa.droidfluxlib.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;
import com.whisppa.droidfluxlib.utils.StoreHelper;

/**
 * Created by user on 6/4/2015.
 */
abstract public class StoreActivity extends AppCompatActivity implements StoreListener {

    private StoreHelper mStoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStoreHelper = new StoreHelper(this, getFlux(), getStores());
    }

    protected void onResume() {
        super.onResume();
        mStoreHelper.onResume();
    }

    protected void onStop() {
        super.onStop();
        mStoreHelper.onStop();
    }

    protected abstract String[] getStores();
    protected abstract Flux getFlux();

}
