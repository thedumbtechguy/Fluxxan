package com.whisppa.droidflux;

import android.app.Application;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Store;

/**
 * Created by user on 5/8/2015.
 */
public class MyApp extends Application {

    static Flux<MyActions> DroidFlux;

    @Override
    public void onCreate() {
        super.onCreate();

        DroidFlux = new Flux<MyActions>(new Store[]{new MyStore(), new MyOtherStore()}, new MyActions());
        DroidFlux.getDispatcher().start();
    }

    public void onTerminate() {
        super.onTerminate();

        DroidFlux.getDispatcher().stop();
    }

    public static Flux<MyActions> getFlux() {
        return DroidFlux;
    }
}
