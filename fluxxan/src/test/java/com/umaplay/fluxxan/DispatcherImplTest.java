package com.umaplay.fluxxan;

import com.umaplay.fluxxan.impl.BaseActionCreator;
import com.umaplay.fluxxan.impl.BaseMiddleware;
import com.umaplay.fluxxan.impl.BaseReducer;
import com.umaplay.fluxxan.impl.DispatcherImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class DispatcherImplTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private DispatcherImpl<Boolean> mDispatcher;

    class MyReducer extends BaseReducer<Boolean> {
        @Override
        public DispatchResult<Boolean> reduce(Boolean state, Action action) throws Exception {
            return new DispatchResult<Boolean>(!state, true);
        }
    }

    @Before
    public void setUp() {
        Boolean state = false;
        mDispatcher = new DispatcherImpl<>(state);
        mDispatcher.start();
    }

    @After
    public void cleanUp() {
        mDispatcher.stop();
    }

    @Test
    public void getStateWorks() throws Exception {
        assertFalse(mDispatcher.getState());
    }

    @Test
    public void dispatchDeliveryToReducers() throws Exception {
        MyReducer reducer = new MyReducer();
        mDispatcher.registerReducer(reducer);

        StateListener listener = mock(StateListener.class);
        mDispatcher.addListener(listener);

        dispatch(new Action("EMPTY_ACTION"));

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(listener, times(1)).hasStateChanged(captor.capture(), anyBoolean());

        assertEquals(true, captor.getValue());

        mDispatcher.removeListener(listener);
        mDispatcher.unregisterReducer(MyReducer.class);
    }

    @Test
    public void registerReducerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mDispatcher.registerReducer(reducer);

        assertEquals(mDispatcher.getReducer(MyReducer.class), reducer);

        mDispatcher.unregisterReducer(MyReducer.class);
    }

    @Test
    public void unregisterReducerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mDispatcher.registerReducer(reducer);
        mDispatcher.unregisterReducer(MyReducer.class);
        assertNull(mDispatcher.getReducer(MyReducer.class));
    }

    @Test
    public void registerMiddlewareWorks() throws Exception {
        Middleware middleware = mock(Middleware.class);

        mDispatcher.registerMiddleware(middleware);
        Action action = new Action("EMPTY_ACTION");
        dispatch(action);

        ArgumentCaptor<Boolean> stateCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass(Action.class);
        verify(middleware, times(1)).intercept(stateCaptor.capture(), actionCaptor.capture());

        assertEquals(action, actionCaptor.getValue());
        assertEquals(false, stateCaptor.getValue());

        mDispatcher.unregisterMiddleware(Middleware.class);
    }

    @Test
    public void shouldNotRegisterTwoMiddlewaresFromSameClass() throws Exception {
        exception.expect(IllegalStateException.class);

        Middleware middleware = mock(Middleware.class);
        mDispatcher.registerMiddleware(middleware);

        Middleware anotherMiddleware = mock(Middleware.class);
        mDispatcher.registerMiddleware(anotherMiddleware);

        mDispatcher.unregisterMiddleware(Middleware.class);
    }

    @Test
    public void unregisterMiddlewareWorks() throws Exception {
        Middleware middleware = mock(Middleware.class);

        mDispatcher.registerMiddleware(middleware);
        mDispatcher.unregisterMiddleware(middleware.getClass());
        dispatch(new Action("EMPTY_ACTION"));

        verify(middleware, never()).intercept(anyObject(), any(Action.class));
    }

    @Test
    public void addListenerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mDispatcher.registerReducer(reducer);

        StateListener listener = mock(StateListener.class);

        mDispatcher.addListener(listener);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);

        dispatch(new Action("EMPTY_ACTION"));

        verify(listener, times(1)).hasStateChanged(anyBoolean(), anyObject());

        mDispatcher.removeListener(listener);
        mDispatcher.unregisterReducer(MyReducer.class);
    }

    @Test
    public void removeListenerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mDispatcher.registerReducer(reducer);

        StateListener listener = mock(StateListener.class);

        mDispatcher.addListener(listener);
        mDispatcher.removeListener(listener);

        dispatch(new Action("EMPTY_ACTION"));

        verify(listener, never()).hasStateChanged(anyBoolean(), anyBoolean());

        mDispatcher.unregisterReducer(MyReducer.class);
    }

    @Test
    public void shouldStartDispatcher() throws Exception {
        DispatcherImpl<Boolean> dispatcher = new DispatcherImpl<>(true);
        dispatcher.start();

        dispatch(dispatcher, new Action("EMPTY_ACTION"));

        dispatcher.unregisterReducer(Reducer.class);
        dispatcher.stop();
    }

    @Test
    public void shouldStopDispatcher() throws Exception {
        exception.expect(IllegalStateException.class);

        DispatcherImpl<Boolean> dispatcher = new DispatcherImpl<>(true);
        dispatcher.start();
        dispatcher.stop();

        Reducer reducer = mock(Reducer.class);
        dispatcher.registerReducer(reducer);

        dispatcher.dispatch(new Action("EMPTY_ACTION"));

        dispatcher.unregisterReducer(MyReducer.class);
    }

    private void dispatch(Action action) {
        dispatch(mDispatcher, action);
    }

    private void dispatch(Dispatcher dispatcher, Action action) {
        dispatcher.dispatch(action);
        while (dispatcher.isDispatching()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}