package com.umaplay.fluxxan;

/**
 * A callback passed to {@link Dispatcher#waitFor}
 */
public interface WaitCallback {
    public void call();
}
