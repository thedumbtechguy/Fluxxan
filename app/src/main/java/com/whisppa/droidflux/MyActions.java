package com.whisppa.droidflux;

import android.os.Bundle;

import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.impl.AbstractActionsImpl;

/**
 * Created by user on 5/8/2015.
 */
public class MyActions extends AbstractActionsImpl {
    public static final String GET_USER = "GET_USER";

    public void getUsers() {
        try {
            mDispatcher.dispatch(new Payload(GET_USER, new Bundle()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
