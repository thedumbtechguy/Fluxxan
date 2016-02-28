package com.umaplay.fluxxan;

/**
 * Created by user on 2/19/2016.
 */
public class DispatchResult<State> {
    public final boolean handled;
    public final State state;

    public DispatchResult(State state, boolean handled) {
        this.handled = handled;
        this.state = state;
    }
}
