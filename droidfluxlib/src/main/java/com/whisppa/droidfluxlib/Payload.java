package com.whisppa.droidfluxlib;

import android.os.Bundle;

/**
 * Created by user on 5/5/2015.
 */
public class Payload {
    public final String Type;
    public final Bundle Data;

    public Payload(String type, Bundle data) {
        Type = type;
        Data = data;
    }
}
