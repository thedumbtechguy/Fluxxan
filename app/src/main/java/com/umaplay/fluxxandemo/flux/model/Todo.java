package com.umaplay.fluxxandemo.flux.model;

import org.immutables.value.Value;

/**
 * Created by user on 2/17/2016.
 */
@Value.Immutable
public abstract class Todo {
    public abstract String getUid();

    public abstract String getTitle();

    public abstract Status getStatus();

    public enum Status {
        OPEN,
        CLOSED
    }
}
