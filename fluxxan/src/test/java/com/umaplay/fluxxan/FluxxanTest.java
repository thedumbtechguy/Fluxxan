package com.umaplay.fluxxan;

import com.umaplay.fluxxan.impl.BaseActionCreator;
import com.umaplay.fluxxan.impl.BaseReducer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class FluxxanTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private Fluxxan<Boolean> mFluxxan;

    class MyReducer extends BaseReducer<Boolean> {
        @Override
        public DispatchResult<Boolean> reduce(Boolean state, Action action) throws Exception {
            return new DispatchResult<Boolean>(!state, true);
        }
    }

    @Before
    public void setUp() {
        Boolean state = false;
        mFluxxan = new Fluxxan(state);
        mFluxxan.start();
    }

    @After
    public void cleanUp() {
        mFluxxan.stop();
    }

    @Test
    public void returnInitialState() throws Exception {
        assertFalse(mFluxxan.getState());
    }

    @Test
    public void dispatcherIsNonNull() throws Exception {
        assertNotNull(mFluxxan.getDispatcher());
    }

    @Test
    public void injectDispatcherWorks() throws Exception {
        BaseActionCreator ac = new BaseActionCreator();
        mFluxxan.inject(ac);
        ac.dispatch(new Action("EMPTY_ACTION"));
    }

    @Test
    public void notInjectDispatcherThrows() throws Exception {
        exception.expect(IllegalStateException.class);
        BaseActionCreator ac = new BaseActionCreator();
        ac.dispatch(new Action("EMPTY_ACTION"));
    }

    @Test
    public void registerReducerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mFluxxan.registerReducer(reducer);

        assertEquals(mFluxxan.getReducer(MyReducer.class), reducer);

        mFluxxan.unregisterReducer(MyReducer.class);
    }

    @Test
    public void unregisterReducerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mFluxxan.registerReducer(reducer);
        mFluxxan.unregisterReducer(MyReducer.class);
        assertNull(mFluxxan.getReducer(MyReducer.class));
    }

    @Test
    public void registerMiddlewareWorks() throws Exception {
        Middleware middleware = mock(Middleware.class);

        Middleware expected = mFluxxan.registerMiddleware(middleware);

        assertEquals(expected, middleware);

        mFluxxan.unregisterMiddleware(middleware.getClass());
    }

    @Test
    public void shouldNotRegisterTwoMiddlewaresFromSameClass() throws Exception {
        exception.expect(IllegalStateException.class);

        Middleware middleware = mock(Middleware.class);
        mFluxxan.registerMiddleware(middleware);

        Middleware anotherMiddleware = mock(Middleware.class);
        mFluxxan.registerMiddleware(anotherMiddleware);

        mFluxxan.unregisterMiddleware(Middleware.class);
    }

    @Test
    public void unregisterMiddlewareWorks() throws Exception {
        Middleware middleware = mock(Middleware.class);

        mFluxxan.registerMiddleware(middleware);
        Middleware expected = mFluxxan.unregisterMiddleware(middleware.getClass());

        assertEquals(expected, middleware);
    }

    @Test
    public void addListenerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mFluxxan.registerReducer(reducer);

        StateListener listener = mock(StateListener.class);

        mFluxxan.addListener(listener);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);

        dispatch(new Action("EMPTY_ACTION"));

        verify(listener, times(1)).hasStateChanged(captor.capture(), anyBoolean());

        assertEquals(true, captor.getValue());

        mFluxxan.unregisterReducer(MyReducer.class);
    }

    @Test
    public void removeListenerWorks() throws Exception {
        MyReducer reducer = new MyReducer();
        mFluxxan.registerReducer(reducer);

        StateListener listener = mock(StateListener.class);

        mFluxxan.addListener(listener);
        mFluxxan.removeListener(listener);

        dispatch(new Action("EMPTY_ACTION"));

        verify(listener, never()).hasStateChanged(anyBoolean(), anyBoolean());

        mFluxxan.unregisterReducer(MyReducer.class);
    }

    @Test
    public void shouldStartDispatcher() throws Exception {
        Fluxxan<Boolean> fluxxan = new Fluxxan<>(true);
        fluxxan.start();

        dispatch(fluxxan, new Action("EMPTY_ACTION"));

        fluxxan.unregisterReducer(Reducer.class);
    }

    @Test
    public void shouldStopDispatcher() throws Exception {
        exception.expect(IllegalStateException.class);

        Fluxxan<Boolean> fluxxan = new Fluxxan<>(true);
        fluxxan.start();
        fluxxan.stop();

        Reducer reducer = mock(Reducer.class);
        fluxxan.registerReducer(reducer);

        fluxxan.getDispatcher().dispatch(new Action("EMPTY_ACTION"));

        fluxxan.unregisterReducer(MyReducer.class);
    }

    private void dispatch(Action action) {
        dispatch(mFluxxan, action);
    }

    private void dispatch(Fluxxan fluxxan, Action action) {
        fluxxan.getDispatcher().dispatch(action);
        while (fluxxan.getDispatcher().isDispatching()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}