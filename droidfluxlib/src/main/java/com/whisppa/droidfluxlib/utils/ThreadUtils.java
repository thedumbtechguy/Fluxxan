package com.whisppa.droidfluxlib.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by user on 2/12/2016.
 */
public class ThreadUtils {
    public static void ensureNotOnMain(){
        if(Looper.myLooper() == Looper.getMainLooper())
            throw new DispatchOnMainThreadException();
    }

    public static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static class DispatchOnMainThreadException extends RuntimeException {}
}
