package com.whisppa.droidfluxlib.impl;

import android.util.Log;

import com.whisppa.droidfluxlib.Callback;
import com.whisppa.droidfluxlib.Dispatcher;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.Store;
import com.whisppa.droidfluxlib.StoreListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 5/5/2015.
 */
public abstract class AbstractStoreImpl<State> implements Store<Object> {
    private static final String TAG = "DroidFlux:AbstractStore";
    private Dispatcher mDispatcher;
    private boolean mIsResolved = false;
    private Callback mWaitCallback = null;
    private final ConcurrentHashMap<String, String> mActionMap = new ConcurrentHashMap<String, String>();
    private final List<StoreListener> mListeners;
    private final List<String> mWaitingOnList;

    public AbstractStoreImpl() {
        mListeners = Collections.synchronizedList(new ArrayList<StoreListener>());
        mWaitingOnList = Collections.synchronizedList(new ArrayList<String>());
    }

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    @Override
    public boolean handleAction(Payload payload) throws Exception {
        if(mActionMap.containsKey(payload.Type)) {
            Method method;
            method = this.getClass().getMethod(mActionMap.get(payload.Type), Payload.class);

            //TODO: this should be forced to run on the UI thread
            method.invoke(this, payload);

            return true;
        }

        return false;
    }

    @Override
    public void resetState() {
        mIsResolved = false;
        mWaitingOnList.clear();
        mWaitCallback = null;
    }

    @Override
    public List<String> getWaitingOnList() {
        return mWaitingOnList;
    }

    @Override
    public Callback getWaitCallback() {
        return mWaitCallback;
    }

    @Override
    public void notifyListeners() {
        synchronized (mListeners) {
            Iterator<StoreListener> it = mListeners.iterator(); // Must be in synchronized block
            while (it.hasNext()) {
                try {
                    it.next().onChanged();
                }
                catch (Exception e) {
                    Log.e(TAG, "Unexpected exception during notifyAll", e);
                    throw e;
                }
            }
        }
    }

    @Override
    public boolean isResolved() {
        return mIsResolved;
    }

    @Override
    public boolean setResolved(boolean resolved) {
        return mIsResolved = resolved;
    }

    @Override
    public void addToWaitingOnList(Collection<String> storeNames) {
        mWaitingOnList.addAll(storeNames);
    }


    public void bindActions(HashMap<String, String> actionMap) throws Exception {
        Iterator<Map.Entry<String, String>> it = actionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            bindAction(entry.getKey(), entry.getValue());
        }
    }

    public void bindAction(String actionType, String methodName) throws Exception {
        //TODO: make a check if the method actually exists and is accessible. Wondering it that is a good idea here
        //It should prevent issues when calling the method later on
        // throw new Exception("Invalid method name '" + method + "' passed in actions map");
        mActionMap.put(actionType, methodName);
    }

    @Override
    public boolean addListener(StoreListener storeListener) {
        removeListener(storeListener);
        return mListeners.add(storeListener);
    }

    @Override
    public void removeListener(StoreListener storeListener) {
        mListeners.remove(storeListener);
    }

    @Override
    public void waitFor(String[] storeNames, Callback callback) throws Exception {
        Set<String> _storeNames = new HashSet<String>(Arrays.asList(storeNames));

        mDispatcher.waitFor(this.getClass().getName(), _storeNames, callback);
    };

    public abstract State getState();

}
