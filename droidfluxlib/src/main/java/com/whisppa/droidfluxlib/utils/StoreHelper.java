package com.whisppa.droidfluxlib.utils;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;

/**
 * Created by user on 6/2/2015.
 */
public class StoreHelper {

    private final Flux mFlux;
    private final String[] mStores;
    private final StoreListener mListener;

    public StoreHelper(StoreListener listener, Flux flux, String[] stores) {
        mFlux = flux;
        mStores = stores;
        mListener = listener;
    }

    public void onResume() {
        for(int i = 0; i < mStores.length; i++) {
            mFlux.getStore(mStores[i]).addListener(mListener);
        }
    }


    public void onStop() {
        for(int i = 0; i < mStores.length; i++) {
            mFlux.getStore(mStores[i]).removeListener(mListener);
        }
    }
}
