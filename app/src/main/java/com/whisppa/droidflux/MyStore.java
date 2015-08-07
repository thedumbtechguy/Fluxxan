package com.whisppa.droidflux;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.annotation.BindAction;
import com.whisppa.droidfluxlib.impl.AbstractStoreImpl;

import java.util.Random;

/**
 * Created by user on 5/8/2015.
 */
public class MyStore extends AbstractStoreImpl<String> {
    String user = "User: Default";

    @BindAction(MyActions.GET_USER)
    public void getUser(Object pl) {
        user = "User: " + new Random().nextInt();
        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADED)
    public void getUserAsyncLoaded(MyActions.User payload) {
        user = "User Async: " + payload.ID;
        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADING)
    public void getUserAsyncLoading(Object pl) {
        user = "Loading...";
        this.notifyListeners();
    }

    @Override
    public String getState() {
        return user;
    }

    @Override
    public Flux getFlux() {
        return MyApp.getFlux();
    }

}
