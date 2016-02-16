package com.umaplay.droidflux;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

/**
 * Created by user on 5/8/2015.
 */
public class MyAnnotatedReducer extends BaseAnnotatedReducer<MyState> {
    private int num = 0;

    @BindAction(MyActions.GET_USER)
    public MyState getUser(MyState state, Object pl) {
        num --;

        state.StateTwo.user = "User: " + num;
        state.StateTwo.isLoading = false;
        state.StateTwo.hasLoaded = true;

        return state;
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADED)
    public MyState getUserAsyncLoaded(MyState state, MyActions.User payload) {
        state.StateTwo.user = "User Async: " + payload.ID;
        state.StateTwo.isLoading = false;
        state.StateTwo.hasLoaded = true;

        return state;
    }

    @BindAction(MyActions.GET_USER_ASYNC_LOADING)
    public MyState getUserAsyncLoading(MyState state, Object pl) {
        state.StateTwo.user = "";
        state.StateTwo.hasLoaded = false;
        state.StateTwo.isLoading = true;

        return state;
    }


}
