package com.umaplay.droidflux;

import android.app.Application;

import com.umaplay.fluxxan.Flux;

/**
 * Created by user on 5/8/2015.
 */
public class MyApp extends Application {

    static Flux<MyState, MyActions> droidFlux;

    @Override
    public void onCreate() {
        super.onCreate();

        droidFlux = new Flux<>(new MyState(), new MyActions());
        droidFlux.registerReducer(new MyReducer());
        droidFlux.registerReducer(new MyAnnotatedReducer());

        droidFlux.getDispatcher().start();
    }

    public void onTerminate() {
        super.onTerminate();

        droidFlux.getDispatcher().stop();
    }

    public static Flux<MyState, MyActions> getFlux() {
        return droidFlux;
    }
}
