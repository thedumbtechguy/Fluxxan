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
    public void resetState();

    public boolean isResolved();
    public boolean setResolved(boolean resolved);

    public List<String> getWaitingOnList();
    public void addToWaitingOnList(Collection<String> storeNames);

    public Callback getWaitCallback();
    public void setWaitCallback(Callback callback);
    public void waitFor(String[] storeNames, Callback callback)  throws Exception;

    public State getState();
    public Flux getFlux();


    public boolean addListener(StoreListener storeListener);
    public void removeListener(StoreListener storeListener);

    //ensure that this notifies the listeners from the UI thread since it can be called from any thread
    public void notifyListeners();
}