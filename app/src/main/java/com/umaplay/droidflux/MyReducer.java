package com.umaplay.droidflux;

import com.umaplay.fluxxan.Payload;
import com.umaplay.fluxxan.impl.BaseReducer;
import com.umaplay.fluxxan.impl.DispatcherImpl;

/**
 * Created by user on 5/8/2015.
 */
public class MyReducer extends BaseReducer<MyState> {

    private int num = 0;

    public void getUser(MyState state, Object pl) {
        num++;
        state.StateOne = "User: " + num;
    }

    public void getUserAsyncLoaded(MyState state, MyActions.User payload) {
        state.StateOne = "User Async: " + payload.ID;
    }

    public void getUserAsyncLoading(MyState state, Object pl) {
        state.StateOne = "Loading...";
    }

    @Override
    public DispatcherImpl.DispatchResult<MyState> reduce(MyState state, Payload payload) throws Exception {

        switch (payload.Type) {
            case MyActions.GET_USER:
                getUser(state, payload.Data);
                return new DispatcherImpl.DispatchResult<>(state, true);

            case MyActions.GET_USER_ASYNC_LOADED:
                getUserAsyncLoaded(state, (MyActions.User) payload.Data);
                return new DispatcherImpl.DispatchResult<>(state, true);

            case MyActions.GET_USER_ASYNC_LOADING:
                getUserAsyncLoading(state, payload.Data);
                return new DispatcherImpl.DispatchResult<>(state, true);
        }

        return new DispatcherImpl.DispatchResult<>(state, false);
    }
}
