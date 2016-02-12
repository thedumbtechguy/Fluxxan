package com.whisppa.droidflux;

import android.os.Bundle;

import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.impl.AbstractActionsImpl;

import java.util.Random;

/**
 * Created by user on 5/8/2015.
 */
public class MyActions extends AbstractActionsImpl {
    public static final String GET_USER = "GET_USER";
    public static final String GET_USER_ASYNC_LOADED = "GET_USER_ASYNC_LOADED";
    public static final String GET_USER_ASYNC_LOADING = "GET_USER_ASYNC_LOADING";

    public void getUser() {
        try {
            mDispatcher.dispatch(new Payload<Object>(GET_USER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int num = 0;
    public void getUserAsync() {
        try {
            //notify the ui that we are loading
            mDispatcher.dispatch(new Payload<Object>(GET_USER_ASYNC_LOADING));

            //fetch the actual data
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(2000);
                        num++;

                        User bundle = new User();
                        bundle.ID = num;
                        mDispatcher.dispatch(new Payload<>(GET_USER_ASYNC_LOADED, bundle));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class User
    {
        public int ID;
    }

}
