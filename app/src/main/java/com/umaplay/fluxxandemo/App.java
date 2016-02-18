package com.umaplay.fluxxandemo;

import android.app.Application;

import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Flux;
import com.umaplay.fluxxan.impl.DispatcherImpl;
import com.umaplay.fluxxandemo.flux.actioncreator.TodoActionCreator;
import com.umaplay.fluxxandemo.flux.model.AppState;
import com.umaplay.fluxxandemo.flux.model.ImmutableAppState;
import com.umaplay.fluxxandemo.flux.reducer.TodoReducer;

/**
 * Created by user on 5/8/2015.
 */
public class App extends Application {

    static Flux<AppState, TodoActionCreator> Fluxxan;

    @Override
    public void onCreate() {
        super.onCreate();

        AppState state = ImmutableAppState.builder().build();

        Fluxxan = new Flux<AppState, TodoActionCreator>(state, new TodoActionCreator()) {
            protected Dispatcher<AppState> initDispatcher(AppState state) {
                return new DispatcherImpl<AppState>(state) {
                    @Override
                    public boolean hasStateChanged(AppState newState, AppState oldState) {
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

    public static Flux<AppState, TodoActionCreator> getFlux() {
        return Fluxxan;
    }
}
