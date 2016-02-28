package com.umaplay.fluxxan;

/**
 * An action that can be dispatched
 * It defines a Type and an optional data Payload
 *
 * @param <PayLoadType> The type of data that this action carries
 */
public class Action<PayLoadType> {
    public final String Type;
    public final PayLoadType Payload;

    public Action(String type, PayLoadType payload) {
        Type = type;
        Payload = payload;
    }

    public Action(String type) {
        Type = type;
        Payload = null;
    }
}
