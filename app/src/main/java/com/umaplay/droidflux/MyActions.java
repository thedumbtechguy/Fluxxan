package com.umaplay.droidflux;

import com.umaplay.fluxxan.Payload;
import com.umaplay.fluxxan.impl.BaseActions;

/**
 * Created by user on 5/8/2015.
 */
public class MyActions extends BaseActions {
    public static final String GET_USER = "GET_USER";
    public static final String GET_USER_ASYNC_LOADED = "GET_USER_ASYNC_LOADED";
    public static final String GET_USER_ASYNC_LOADING = "GET_USER_ASYNC_LOADING";

    public void getUser() {
        try {
            mDispatcher.dispatch(Creator.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int num = 0;
    public void getUserAsync() {
        try {
            //notify the ui that we are loading
            mDispatcher.dispatch(Creator.loadUser());

            //fetch the actual data
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(2000);
                        num++;

                        User user = new User();
                        user.ID = num;
                        mDispatcher.dispatch(Creator.loadedUser(user));
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

    public static class Creator {
        public static Payload<Object> loadUser() {
            return new Payload<>(GET_USER_ASYNC_LOADING);
        }

        public static Payload<User> loadedUser(User user) {
            return new Payload<>(GET_USER_ASYNC_LOADED, user);
        }

        public static Payload<Object> getUser() {
            return new Payload<>(GET_USER);
        }
    }

}
