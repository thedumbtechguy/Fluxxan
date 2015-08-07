package com.whisppa.droidfluxlib;

import android.support.annotation.NonNull;

import com.whisppa.droidfluxlib.impl.DispatcherImpl;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 5/5/2015.
 */
public class Flux<Acts extends Actions> {

    private final DispatcherImpl mDispatcher;
    private final Acts mActions;
    protected final ConcurrentHashMap<String, Store> mStores = new ConcurrentHashMap<>();

    public Flux(@NonNull Store[] stores, Acts actions) {
        mDispatcher = new DispatcherImpl();

        actions.setDispatcher(mDispatcher);
        mActions = actions;

        addStores(stores);
    }

    public Flux(@NonNull ConcurrentHashMap<String, Store> stores, Acts actions) {
        mDispatcher = new DispatcherImpl();

        actions.setDispatcher(mDispatcher);
        mActions = actions;

        addStores(stores);
    }

    protected void addStores(@NonNull Store[] stores) {
        for(int i = 0; i < stores.length; i++) {
            Store store = stores[i];
            addStore(store);
        }
    }

    public void addStore(@NonNull Store store) {
        addStore(store.getClass().getName(), store);
    }

    public void addStores(@NonNull Map<String, Store> stores) {
        Iterator<Map.Entry<String, Store>> it = stores.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Store> entry = it.next();
            addStore(entry.getKey(), entry.getValue());
        }
    }

    public void addStore(@NonNull String name, @NonNull Store store) {
        store.setDispatcher(mDispatcher);
        getStores().put(name, store);
        mDispatcher.addStore(name, store);
    }

    public void removeStore(Class store) {
        getStores().remove(store.getName());
    }

    protected ConcurrentHashMap<String, Store> getStores() {
        return mStores;
    }

    public <T extends Store> T getStore(Class<T> store) {
        return (T) getStores().get(store.getName());
    }

    public Acts getActions() {
        return mActions;
    }

    public Dispatcher getDispatcher() {
        return mDispatcher;
    }
}
