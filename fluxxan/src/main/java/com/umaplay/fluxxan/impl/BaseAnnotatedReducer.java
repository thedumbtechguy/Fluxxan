package com.umaplay.fluxxan.impl;

import android.text.TextUtils;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.DispatchResult;
import com.umaplay.fluxxan.annotation.BindAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@inheritDoc}
 * This abstract implementation relies on using annotations to determine how to reduce dispatched actions
 * It does this by overriding {@link #reduce(Object, Action)} and calling relevant methods to handle the action
 * During instantiation, methods annotated with {@link BindAction} are read and stored in a {@link ConcurrentHashMap} with the action type as the key
 * When an action is dispatched, the action type is checked against this map and if found, the method is invoked using reflection.
 * Each bound method should the signature `State methodName(State state, PayloadType payload)` or it will fail at runtime.
 *
 * Inheriting classes must remember to call `super()` in the constructor to ensure that annotations are processed
 *
 * @param <State>
 */
public abstract class BaseAnnotatedReducer<State> extends BaseReducer<State> {

    private static final String TAG = "BaseAnnotatedReducer";
    protected final ConcurrentHashMap<String, Method> mActionMap = new ConcurrentHashMap<>();

    //ensure you call super else!!!
    public BaseAnnotatedReducer() {
        super();

        Method[] methods = this.getClass().getMethods();//get only public methods
        for (Method m : methods) {
            if (m.isAnnotationPresent(BindAction.class)) {

                String methodName = m.getName();

                Annotation annotation = m.getAnnotation(BindAction.class);
                BindAction actionAnnotation = (BindAction) annotation;
                String actionName = actionAnnotation.value();

                if(TextUtils.isEmpty(actionName))
                    throw new IllegalArgumentException("BindAction value cannot be empty");

                Class<?>[] parameterTypes = m.getParameterTypes();

                if(parameterTypes.length != 2)
                    throw new InvalidParameterException(String.format("Bound method '%s' must accept t: State and Objectwo arguments", methodName));//let's just use this exception type for want of a better option

                //Best we can do here is ensure that the return type is not void thanks to type erasure
                if(m.getReturnType().equals(Void.TYPE))
                    throw new InvalidParameterException(String.format("Bound method '%s' must return an instance of the State", methodName));

                bindAction(actionName, m);
            }
        }
    }

    @Override
    public DispatchResult<State> reduce(State state, Action action) throws Exception {
        if(mActionMap.containsKey(action.Type)) {
            Method method;
            method = mActionMap.get(action.Type);

            return new DispatchResult<>((State) method.invoke(this, state, action.Payload), true);
        }

        return new DispatchResult<>(state, false);
    }

    protected void bindAction(String actionType, Method method) {
        mActionMap.put(actionType, method);
    }

}
