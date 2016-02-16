package com.umaplay.fluxxan;

/**
 * A payload to specify an action to be dispatched
 * @param <Load> The type of data that this payload carries
 */
public class Payload<Load> {
    public final String Type;
    public final Load Data;

    public Payload(String type, Load data) {
        Type = type;
        Data = data;
    }

    public Payload(String type) {
        Type = type;
        Data = null;
    }
}
