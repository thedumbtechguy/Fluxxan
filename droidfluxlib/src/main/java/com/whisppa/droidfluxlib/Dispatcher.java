package com.whisppa.droidfluxlib;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by user on 5/6/2015.
 */
public interface Dispatcher {
    public void start();
    public void stop();
    public void addStore(@NonNull String name, @NonNull Store store);
    public void dispatch(@NonNull Payload payload);
    public void waitFor(Class waitingStore, Set<Class> stores, Callback callback) throws Exception;
    public boolean isDispatching();
}
