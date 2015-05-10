package com.whisppa.droidflux;

import android.app.Application;

import com.whisppa.droidfluxlib.Flux;
import com.whisppa.droidfluxlib.Store;

/**
 * Created by user on 5/8/2015.
 */
public class MyApp extends Application {

    static Flux<MyActions> FluxDroid;

    @Override
    public void onCreate() {
        FluxDroid = new Flux<MyActions>(new Store[]{new MyStore(), new MyOtherStore()}, new MyActions());
    }

    public static Flux<MyActions> getFlux() {
        return FluxDroid;
    }
}
