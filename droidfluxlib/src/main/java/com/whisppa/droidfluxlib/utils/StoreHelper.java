package com.whisppa.droidfluxlib.utils;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.StoreListener;

/**
 * Created by user on 6/2/2015.
 */
public class StoreHelper {

    private final Flux mFlux;
    private final Class[] mStores;
    private final StoreListener mListener;

    public StoreHelper(StoreListener listener, Flux flux, Class[] stores) {
        mFlux = flux;
        mStores = stores;
        mListener = listener;
    }

    public void onStart() {
        for(int i = 0; i < mStores.length; i++) {
            mFlux.getStore(mStores[i]).addListener(mListener);
        }

        mListener.onChanged();//automatically call onChanged to trigger updates
    }


    public void onStop() {
        for(int i = 0; i < mStores.length; i++) {
            mFlux.getStore(mStores[i]).removeListener(mListener);
        }
    }
}
