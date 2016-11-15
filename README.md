# Fluxxan

Fluxxan is an Android implementation of the Flux Architecture that combines concepts from both Fluxxor and Redux with minimal dependencies and support for Android SDK 8.

Originally DroidFlux, it started as a direct Android port of the popular [Fluxxor](http://fluxxor.com) library seeking to implement the [Flux Architecture](https://facebook.github.io/flux/) as popularised by Facebook for Android.
The library has evolved into a hybrid of the original Flux and [Redux](https://github.com/reactjs/redux), borrowing some of the great ideas from Redux while trying to be as close to the original dictates of Flux as much as possible. 
I ended up with something that looks like Flux, but works a lot like Redux.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Fluxxan-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1786) [![Download](https://api.bintray.com/packages/frostymarvelous/maven/fluxxan/images/download.svg) ](https://bintray.com/frostymarvelous/maven/fluxxan/_latestVersion) 

## Users

Apps using Fluxxan in production 

[![Umaplay](assets/uma_logo.png)](https://play.google.com/store/apps/details?id=com.umaplay.android)


### Current Version: 1.0.0

Fluxxan follows [Semantic Versioning](http://semver.org/).

[![](https://www.bintray.com/docs/images/bintray_badge_color.png)](https://bintray.com/frostymarvelous/maven/fluxxan/view?source=watch)

### Fluxxan4j

If you are looking for a pure java implementation, you can check out [Fluxxan4j](https://github.com/frostymarvelous/Fluxxan4j) which strips out the android dependencies and supports Java 6.


## Installation

#### Gradle
Fluxxan is available on jcenter.

```gradle
compile 'com.umaplay.oss:fluxxan:1.0.0'
```

#### Manual Installation
Download the [aar artifact](artifacts/fluxxan-1.0.0.aar) from the [artifacts](artifacts/) directory
and copy it into the libs directory of your app module.

Specify `libs` as a repository in your root gradle file.
```groovy
    allprojects {
        repositories {
            ...
            flatDir { dirs 'libs' }
        }
    }
``` 
   
Specify Fluxxan as dependency in your app's gradle file.
```groovy
    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        compile(name: 'fluxxan-1.0.0', ext: 'aar')
        ...
    }
```

## Introduction
 I won't attempt to teach you the concepts of Flux. There are enough articles on the internet for that. Facebook has a great [introduction to flux](https://facebook.github.io/flux/docs/overview.html) to get you started.
 I will instead focus on showing you how to achieve Flux using Fluxxan. In this introduction tutorial, I will walk you through building the sample Todo app included in the source.

Fluxxan is composed of the

1. State
2. Dispatcher
3. Actions
4. Action Creators
5. Reducers (called stores in traditional Flux)
6. StateListeners
7. Middlewares

#### How it works
You hold your application state in a single `State` object tree. In order to update the `State`, you tell an `ActionCreator`, which creates and dispatches an `Action` object describing what happened. 
The `Dispatcher` calls all registered middlewares in the same registered order to intercept the actions been dispatched and perform async operations. Following the `Dispatcher` calls all registered `Reducer`s with the `State` and `Action ` and each `Reducer` specifies how the `Action` transforms the state tree.
`StateListeners` are passed the returned `State` and update the UI accordingly.

### State
The `State` is the Single Source of Truth of your application. It a single object tree containing the entire app state.

> Unlike Redux, Fluxxan does not force you to use an immutable state even though the default implementation assumes you do.
> It is greatly encouraged you do as it will both improve your code, debugging and increase the overall performance of your application. There is a reason most of the new Flux implementations are going immutable.
> If you choose not to go Immutable, you will need to override couple of methods to help you short circuit the dispatch process.

I've found [Immutables](http://immutables.github.io/) to be a really great way to achieve Immutability. It's quick to setup and understand.

It’s a good idea to think of its shape before writing any code. 
For our Todo app will hold a list of Todo items and a Filter to define which Todos to show.

```java
    @Value.Immutable
    public abstract class AppState {
    
        @Value.Parameter
        public abstract Map<String, Todo> getTodos();
    
        @Value.Default
        public Filter getFilter() {
            return Filter.ALL;
        }
    
        public enum Filter {
            ALL,
            OPEN,
            CLOSED
        }
    }
```
We also define our `Todo` object as an Immutable.
```java
    @Value.Immutable
    public abstract class Todo {
        @Value
        public abstract String getUid();
    
        @Value
        public abstract String getTitle();
    
        @Value
        public abstract Status getStatus();
    
        public enum Status {
            OPEN,
            CLOSED
        }
    }
```
 When we build our project, `Immutables` will generate concrete immutable versions of our `AppState` and `Todo` models prefixed with "Immutable" to give `ImmutableAppState` and `ImmutableTodo`.

### Actions
[Action](fluxxan/src/main/java/com/umaplay/fluxxan/Action.java)s are objects that define a `Type` and a data `Payload`.

The action `Type` is a unique string to identify the given action and the `Payload` is any object that you wish to pass to the `Reducer`.

`Action`s are created by `ActionCreator`s and passed to the `Dispatcher` which in turn passes it to each reducer.

### Action Creators
As the name implies, `Action Creators` are methods that create and dispatch `Actions`. 

>Fluxxan does not dictate how you create your `Action Creators`. You have full freedom in this regard. They can be static methods or instance methods. 

We have some guidelines which you can follow. 

1. Create a class to group related action creators. e.g. in our app we will create a `TodoActionCreator` class.
2. Use instances of each creator rather than static methods.
3. Each creator should have a nested `Creator` class that handles the actual creation of the `Action`s using static methods. This gives our `Action Creator`s two roles and allows to dispatch multiple actions or async tasks while still ensuring we can test our `Action`s easily. 
4. Nested creator classes should be composed of pure static methods.  They should return the same `Action` each time when given the same arguments. They should cause no side effects.
5. Don't limit your creators to only dispatch `Actions`. They are an opportunity to centralize all actions that are taken from the ui. e.g. In a music player app, you can have a `PlayerActionCreator` class that has a  `play()` method that tells the media player to start playing and does not dispatch an action. Technically, this is not an `Action Creator` but it's nice that we can have all interactions with the player in one single place.

Let's see what we have in our `Todo` app (*simplified for brevity*).

```java
    public class TodoActionCreator extends BaseActionCreator {
        public static final String ADD_TODO = "ADD_TODO";
    
        public void addTodo(String todo) {
	        //get and dispatch the action from our creator
            dispatch(Creator.addTodo(todo));
        }
        
        public static class Creator {
            public static Action<String> addTodo(String todo) {
                return new Action<>(ADD_TODO, todo);
            }
        }
    }
```

We extend [BaseActionCreator](fluxxan/src/main/java/com/umaplay/fluxxan/impl/BaseActionCreator.java) which gives us `dispatch(Action)`. Remember to set the dispatcher on your Action Creator after instantiation using `setDispatcher(Dispatcher)` or `Fluxxan.inject(ActionCreator)`

As we can see, when we call `addTodo`, our creator gets the relevant action from the nested creator and dispatches it. We can do neat things in `addTodo` if we wanted like post to a web service.

In pseudo code it would look something like this:

```java
    public void addTodo(String todo) {
   	    //save on the server
		postToWebservice(todo)
			.onStarted(c => dispatch(Creator.addTodoStarted(todo)))
			.onSuccess(c => dispatch(Creator.addTodoSuccess(todo)))
			.onFailure(c => dispatch(Creator.addTodoFailed(todo)));
    }
```

Since we are using a dedicated Creator, this allows us to test the actions without having to mock the dispatcher or the web service. We can simply do anywhere in our code:

```java
    dispatch(TodoActionCreator.Creator.addTodoStarted(todo));
    dispatch(TodoActionCreator.Creator.addTodoSuccess(todo));
    dispatch(TodoActionCreator.Creator.addTodoFailed(todo));
```

###Reducers
`Reducer`s describe how our `State` changes in response to an `Action`. Like ActionCreator Creators, Reducers need to be pure. That means, no side effects, no calling of an API etc. They should rely solely on the Action to transform the state.
Given the same arguments, Reducers should return the same result each time.

To register a `Reducer`, you need to call `Dispatcher.registerReducer(Reducer)` and `Dispatcher.unregisterReducer(Reducer)` if you wish to remove it.

`Reducer`s implement the [Reducer](fluxxan/src/main/java/com/umaplay/fluxxan/Reducer.java) interface. 
We provide two abstract implementations: `BaseReducer` and `BaseAnnotatedReducer` both coupled to the default `Dispatcher` implementation.

`BaseReducer` requires you to implement `reduce(State, Action)` in which you can check if you want to handle that action `Type`.

```java
     @Override
     public DispatchResult<State> reduce(State state, Action action) throws Exception {
    
           if(action.Type.equals(TodoActionCreator.ADD_TODO)) {
               //do your thing here
               
               //return the new state and indicate that we handled this action
               return new DispatchResult<>(newState, true);
           }
    
           return new DispatchResult<>(state, false);
       }
```

`BaseAnnotatedReducer` uses reflection to determine handlers for each action and calls them for you. This keeps your code cleaner and more concise.
You annotate the method with `@BindAction(String)` and ensure the method has the signature `State methodName(State state, PayloadType payload)`.

This is what our reducer looks like.

```java
    public class TodoReducer extends BaseAnnotatedReducer<AppState> {
    
        @BindAction(TodoActionCreator.ADD_TODO)
        public AppState addTodo(AppState state, String todo) {
            Todo iTodo = ImmutableTodo.builder()
                    .uid(UUID.randomUUID().toString())
                    .title(todo)
                    .status(Todo.Status.OPEN)
                    .build();
    
            return ImmutableAppState.builder()
                    .from(state)
                    .putTodos(iTodo.getUid(), iTodo)
                    .build();
        }
    }
```

You can call `Dispathcer.waitFor` or the convenience method provided by `BaseReducer` and by extension `BaseAnnotatedReducer`.  
This allows the reducer to ensure that other reducers run before it.

### StateListener
A `StateListener` register's itself with the `Dispatcher` to be notified each time the `State` changes. It must implement the `StateListener` interface. It can be any object including an Activity, Fragment, View or Service (running in the same process) etc.

A listener is added using the `Dispatcher.addListener(StateListener)` and `Dispatcher
.removeListener(StateListener)` to remove it.

`hasStateChanged(State newState, State oldState)` is a convenience method to help short-circuit the dispatch process if we aren't using an immutable state. You can localize your checks to certain nodes of the state tree specific to this listener. 
Since we assume your state is immutable, the default implementations use `return newState != oldState`. If this returns false, `onStateChanged` is not called.  

`onStateChanged(final State state)` is called when the state has changed. This is not called on the `Main` or `UI` thread but on a dedicated background thread. If you wish to make any changes to any UI element, you will need to post a runnable. We provide you a utility method for this using `ThreadUtils.runOnMain(Runnable)`. 
This design choice was made intentionally to allow you to be able to do any processing (like loops) you wish off the main thread before updating the UI. This allows the UI to remain responsive all the time. 	 

We have base implementations like `StateListenerActivity`, `StateListenerFragment` and `StateListenerView` that take care of handling the lifecycle and registering and unregistering of the listener.

###Middlewares
`Middleware`s [provides a third-party extension point between dispatching an action, and the moment it reaches the reducer](http://redux.js.org/docs/advanced/Middleware.html). 
The middlewares represents a good place for logging, crash reporting, talking to an asynchronous API, interact with databases, and more.

To register a `Middleware`, you need to call `Dispatcher.registerMiddleware(Middleware)` and `Dispatcher.unregisterMiddleware(Middleware)` if you wish to remove it.

`Middleware`s implement the [Middleware](fluxxan/src/main/java/com/umaplay/fluxxan/Middleware.java) interface. 
We provide one abstract implementations: `BaseMiddleware` coupled to the default `Dispatcher` implementation.

`BaseMiddleware` requires you to implement `intercept(State, Action)` in which you can check if you want to handle that action `Type`.

```java
  public LoggerMiddleware extends BaseMiddleware {
     @Override
     public void intercept(State state, Action action) throws Exception {
        Log.d("[LoggerMiddleware]", action.Type);
     }
  }
```

### Dispatcher
We saved the best for last.

The dispatcher is the engine of Fluxxan. It manages the state, reducers and listeners, handles the dispatching of actions and the subsequent notifying of listeners.

After we create our dispatcher, we need to keep a reference to it so we can register `Reducer`s and `StateListener`s as well as dispatch `Action`s.
We need to call `start` and `stop` on the dispatcher to start or stop it.

Ideally, this would be done in a custom android Application so we can get a reference to it anywhere in our code.

The dispatcher checks if states have changed before notifying listeners, but since it assumes state is immutable, you will need to override it's `hasStateChanged(State newState, State oldState)` method to provide your own functionality.
By default, it uses, `return newState != oldState`.

>The dispatcher allows us to provide the ability for a `Reducer` to wait for other reducers. This is an important feature in Flux not required in Redux. 

#### Fluxxan
We provide you a coordinator to help manage the dispatcher. It's called `Fluxxan`.
Fluxxan is used in default implementations so instead of dealing with the `Dispatcher` directly (you can if you choose to) we use `Fluxxan`.
`Fluxxan` provides proxy methods that call `Dispatcher`. By default, it uses the `DispatcherImpl` implementation.

In our app, let's see what this looks like.

```java
    AppState state = ImmutableAppState.builder().build();
    
    Fluxxan = new Fluxxan<AppState>(state);
    Fluxxan.registerReducer(new TodoReducer());
    
    Fluxxan.start();
```

### Proguard

For proguard, you will need to add this.
```proguard
	-keepclasseswithmembers class * {
	    @com.umaplay.fluxxan.annotation.BindAction <methods>;
	}
```

### Contributing

Thank you for taking the time to contribute.
But before you do, please read our [contribution guidelines](CONTRIBUTING.md). They are simple, we promise.


### Todo
  - Writing Tests

	
### License
MIT
