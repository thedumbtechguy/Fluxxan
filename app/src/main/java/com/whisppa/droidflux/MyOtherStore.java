package com.whisppa.droidflux;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.impl.AbstractStoreImpl;

import java.util.Random;

/**
 * Created by user on 5/8/2015.
 */
public class MyOtherStore extends AbstractStoreImpl<MyOtherStore.MyState> {
    MyState myState = new MyState();

    public MyOtherStore() {
        try {
            bindAction(MyActions.GET_USER, "getUser");
            bindAction(MyActions.GET_USER_ASYNC_LOADING, "getUserAsyncLoading");
            bindAction(MyActions.GET_USER_ASYNC_LOADED, "getUserAsyncLoaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUser(Payload payload) {
        myState.user = "User: " + new Random().nextInt();
        myState.isLoading = false;
        myState.hasLoaded = true;

        this.notifyListeners();
    }

    public void getUserAsyncLoaded(Payload payload) {
        myState.user = "User Async: " + payload.Data.getInt("ID");
        myState.isLoading = false;
        myState.hasLoaded = true;

        this.notifyListeners();
    }

    public void getUserAsyncLoading(Payload payload) {
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
