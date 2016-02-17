package com.umaplay.fluxxandemo;

import android.app.Application;

import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Flux;
import com.umaplay.fluxxan.impl.DispatcherImpl;
import com.umaplay.fluxxandemo.flux.action.TodoActions;
import com.umaplay.fluxxandemo.flux.model.ImmutableAppState;
import com.umaplay.fluxxandemo.flux.reducer.TodoReducer;

/**
 * Created by user on 5/8/2015.
 */
public class App extends Application {

    static Flux<ImmutableAppState, TodoActions> Fluxxan;

    @Override
    public void onCreate() {
        super.onCreate();

        ImmutableAppState state = ImmutableAppState.builder().build();

        Fluxxan = new Flux<ImmutableAppState, TodoActions>(state, new TodoActions()) {
            protected Dispatcher<ImmutableAppState> initDispatcher(ImmutableAppState state) {
                return new DispatcherImpl<ImmutableAppState>(state) {
                    @Override
                    public boolean hasStateChanged(ImmutableAppState newState, ImmutableAppState oldState) {
                        return newState != oldState;
                    }
                };
            }
        };
        Fluxxan.registerReducer(new TodoReducer());

        Fluxxan.getDispatcher().start();
    }

    public void onTerminate() {
        super.onTerminate();

        Fluxxan.getDispatcher().stop();
    }

    public static Flux<ImmutableAppState, TodoActions> getFlux() {
        return Fluxxan;
    }
}
