package com.umaplay.fluxxandemo.flux.model;

import org.immutables.value.Value;

import java.util.Map;

/**
 * Created by user on 2/17/2016.
 */
@Value.Immutable
public abstract class AppState {

    @Value.Parameter
    public abstract Map<String, Todo> getTodos();

    @Value.Default
    public Visibility getVisibility() {
        return Visibility.ALL;
    }

    public enum Visibility {
        ALL,
        OPEN,
        CLOSED
    }
}
