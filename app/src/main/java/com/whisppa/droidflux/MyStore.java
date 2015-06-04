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
    public void getUser(Payload payload) {
        user = "User: " + new Random().nextInt();
        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADED)
    public void getUserAsyncLoaded(Payload payload) {
        user = "User Async: " + payload.Data.getInt("ID");
        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADING)
    public void getUserAsyncLoading(Payload payload) {
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
