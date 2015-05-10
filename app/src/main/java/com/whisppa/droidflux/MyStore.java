package com.whisppa.droidflux;

import com.whisppa.droidfluxlib.Dispatcher;
import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.impl.AbstractStoreImpl;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by user on 5/8/2015.
 */
public class MyStore extends AbstractStoreImpl<String> {
    String user = "User: Default";

    public MyStore() {
        try {
            bindAction(MyActions.GET_USER, "getUser");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUser(Payload payload) {
        user = "User: " + new Random().nextInt();
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
