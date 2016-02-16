package com.umaplay.droidflux;

/**
 * Created by user on 2/13/2016.
 */
public class MyState {
    public String StateOne = "";
    public MyAsyncState StateTwo = new MyAsyncState();



    public class MyAsyncState {
        public boolean hasLoaded = false;
        public boolean isLoading = false;
        public String user = "Default";
    }
}
