package com.whisppa.droidfluxlib;

import com.whisppa.droidfluxlib.Callback;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 5/6/2015.
 */
public interface Store<State> {
    public void setDispatcher(Dispatcher dispatcher);
    public boolean handleAction(Payload payload) throws Exception;

    public boolean isResolved();
    public boolean setResolved(boolean resolved);

    public void waitFor(Class[] stores, Callback callback)  throws Exception;
    public void waitFor(Class store, Callback callback)  throws Exception;
    public Callback getWaitCallback();
    public void setWaitCallback(Callback callback);
    public List<String> getWaitingOnList();
    public void addToWaitingOnList(Collection<String> storeNames);

    public boolean addListener(StoreListener storeListener);
    public void removeListener(StoreListener storeListener);
    public void notifyListeners();
    public void notifyListeners(Callback callback);

    public Flux getFlux();
    public State getState();
    public void reset();
}