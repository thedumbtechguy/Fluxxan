package com.whisppa.droidfluxlib;

import android.os.Bundle;

/**
 * Created by user on 5/5/2015.
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
