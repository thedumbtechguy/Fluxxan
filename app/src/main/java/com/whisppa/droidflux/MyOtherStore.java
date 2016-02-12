package com.whisppa.droidflux;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.annotation.BindAction;
import com.whisppa.droidfluxlib.impl.AbstractStoreImpl;

import java.util.Random;

/**
 * Created by user on 5/8/2015.
 */
public class MyOtherStore extends AbstractStoreImpl<MyOtherStore.MyState> {
    private int num = 0;

    MyState myState = new MyState();

    @BindAction(MyActions.GET_USER)
    public void getUser(Object pl) {
        num --;
        myState.user = "User: " + num;
        myState.isLoading = false;
        myState.hasLoaded = true;

        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADED)
    public void getUserAsyncLoaded(MyActions.User payload) {
        myState.user = "User Async: " + payload.ID;
        myState.isLoading = false;
        myState.hasLoaded = true;

        this.notifyListeners();
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADING)
    public void getUserAsyncLoading(Object pl) {
        myState.user = "";
        myState.hasLoaded = false;
        myState.isLoading = true;

        this.notifyListeners();
    }

    @Override
    public MyState getState() {
        return myState;
    }

    @Override
    public Flux getFlux() {
        return MyApp.getFlux();
    }

    public class MyState {
        public boolean hasLoaded = false;
        public boolean isLoading = false;
        public String user = "Default";
    }
}
