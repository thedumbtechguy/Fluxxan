package com.whisppa.droidfluxlib.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.whisppa.droidfluxlib.Callback;
import com.whisppa.droidfluxlib.Dispatcher;
import com.whisppa.droidfluxlib.Payload;
import com.whisppa.droidfluxlib.Store;
import com.whisppa.droidfluxlib.utils.CollectionUtils;
import com.whisppa.droidfluxlib.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by user on 5/5/2015.
 */
public class DispatcherImpl implements Dispatcher {

    private static final String TAG = "DroidFlux:Dispatcher";

    protected final LinkedBlockingQueue<Payload> mDispatchQueue = new LinkedBlockingQueue<>();;
    protected final ConcurrentHashMap<String, Store> mStores = new ConcurrentHashMap<>();
    protected AtomicBoolean mIsDispatching = new AtomicBoolean(false);
    protected String mCurrentActionType = null;
    protected Collection<String> mWaitingToDispatch;

    protected boolean isStarted;
    private Thread mDispatchThread;

    public DispatcherImpl() {
    }

    @Override
    public void start() {
        if(isStarted) return;

        mDispatchThread = new Thread(new DispatchThread());
        mDispatchThread.start();
        isStarted = true;
    }

    @Override
    public void stop() {
        isStarted = false;
        mDispatchThread.interrupt();
        try {
            mDispatchThread.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDispatchThread = null;
    }

    protected void _dispatch(@NonNull final Payload payload) {
        ThreadUtils.ensureNotOnMain();

        String[] names = mStores.keySet().toArray(new String[mStores.size()]);
        for (String name : names) {
            Store store = mStores.get(name);
            store.reset();
        }

        mCurrentActionType = payload.Type;
        mWaitingToDispatch = new HashSet<>(mStores.keySet());

        mIsDispatching.set(true);

        RuntimeException ex = null;
        try {
            Log.i("DroidFlux:Dispatcher", String.format("[STARTED] dispatch of action [%s]", payload.Type));
            doDispatchLoop(payload);
            Log.i("DroidFlux:Dispatcher", String.format("[COMPLETED] dispatch of action [%s]", payload.Type));
        }
        catch (Exception e) {
            Log.e("DroidFlux:Dispatcher", String.format("[FAILED] dispatch of action [%s]", payload.Type), e);
            ex = new RuntimeException(e);
        }
        finally {
            mCurrentActionType = null;
            mIsDispatching.set(false);
        }

        //we need to propagate the exception
        if(ex != null) throw ex;
    }

    protected synchronized void doDispatchLoop(Payload payload) throws Exception {
        ThreadUtils.ensureNotOnMain();

        Store dispatch;
        Boolean canBeDispatchedTo;
        Boolean wasHandled = false;
        List<String> removeFromDispatchQueue = new ArrayList<>();
        List<String> dispatchedThisLoop = new ArrayList<>();

        for (String key : mWaitingToDispatch) {
            dispatch = mStores.get(key);
            canBeDispatchedTo = (dispatch.getWaitingOnList().size() == 0) || (CollectionUtils.intersection(dispatch.getWaitingOnList(), new ArrayList<>(mWaitingToDispatch)).size() == 0);

            if (canBeDispatchedTo) {
                if (dispatch.getWaitCallback() != null) {
                    Callback fn = dispatch.getWaitCallback();
                    dispatch.reset();
                    dispatch.setResolved(true);
                    fn.call();
                    wasHandled = true;
                } else {
                    dispatch.setResolved(true);
                    boolean handled = mStores.get(key).handleAction(payload);
                    if (handled) {
                        wasHandled = true;
                    }
                }

                dispatchedThisLoop.add(key);
                if (dispatch.isResolved()) {
                    removeFromDispatchQueue.add(key);
                }
            }
        }

        if (mWaitingToDispatch.size() > 0 && dispatchedThisLoop.size() == 0) {
            String storesWithCircularWaits = CollectionUtils.implode(mWaitingToDispatch.iterator());
            throw new Exception("Indirect circular wait detected among: " + storesWithCircularWaits);
        }

        for(int i = 0; i < removeFromDispatchQueue.size(); i++)
            mWaitingToDispatch.remove(removeFromDispatchQueue.get(i));

        if (mWaitingToDispatch.size() > 0) {
            this.doDispatchLoop(payload);
        }

        if (!wasHandled) {
            Log.d(TAG, String.format("An action of type [%s] was dispatched, but no store handled it", payload.Type));
        }
    }

    //------ public api

    public void addStore(@NonNull String name, @NonNull Store store) {
        mStores.put(name, store);
    }

    public void dispatch(@NonNull Payload payload) {
        if(!isStarted) {
            throw new IllegalStateException("Dispatcher not started!");
        }

        if (TextUtils.isEmpty(payload.Type)) {
            throw new IllegalArgumentException("Can only dispatch actions with a valid 'Type' property");
        }

        mDispatchQueue.offer(payload);
    }

    public void waitFor(Class waitingStore, Set<Class> storeNames, Callback callback) throws Exception {
        ThreadUtils.ensureNotOnMain();

        String waitingStoreName = waitingStore.getName();

        if (!isDispatching()) {
            throw new Exception("Cannot wait unless an action is being dispatched");
        }

        if (storeNames.contains(waitingStore)) {
            throw new Exception("A store cannot wait on itself");
        }

        Store dispatch = mStores.get(waitingStoreName);

        if (dispatch.getWaitingOnList().size() > 0) {
            throw new Exception(waitingStoreName + " is already waiting on stores");
        }

        for (Class storeName1 : storeNames) {
            String storeName = storeName1.getName();

            if (!mStores.containsKey(storeName)) {
                throw new Exception("Cannot wait for non-existent store " + storeName);
            }

            Store storeDispatch = mStores.get(storeName);
            if (storeDispatch.getWaitingOnList().contains(waitingStoreName)) {
                throw new Exception("Circular wait detected between " + waitingStoreName + " and " + storeName);
            }
        }

        dispatch.reset();

        dispatch.setWaitCallback(callback);
        dispatch.addToWaitingOnList(storeNames);
    }

    public boolean isDispatching() {
        return mIsDispatching.get();
    }


    //------ inner classes

    private final class DispatchThread implements Runnable {
        @Override
        public void run() {
            boolean run = true;
            Payload payload = null;

            while (run) {
                if(mDispatchThread.isInterrupted()) return;

                try {
                    payload = mDispatchQueue.take();
                    if(payload != null) {
                        _dispatch(payload);
                    }
                } catch (InterruptedException e) {
                    run = false;
                }
            }
        }
    }
}
