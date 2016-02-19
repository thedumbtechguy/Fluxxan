# Fluxxan

Fluxxan is an Android implementation of the Flux Architecture that combines concepts from both Fluxxor and Redux.

Originally DroidFlux, it started as a direct Android port of the popular [Fluxxor](http://fluxxor.com) library seeking to implement the [Flux Architecture](https://facebook.github.io/flux/) as popularised by Facebook for Android.
The library has evolved into a hybrid of the original Flux and [Redux](https://github.com/reactjs/redux), borrowing some of the great ideas from Redux while trying to be as close to the original dictates of Flux as much as possible. 
I ended up with something that looks like Flux, but works a lot like Redux.


[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Fluxxan-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1786)

### Current Version: 0.1.0
Starting at 0.1.0 Fluxxan follows [Semantic Versionioning](http://semver.org/).

## Installation

####Gradle

Currently, Fluxxan is not available on maven/jcenter. You will need to use the manual installation in the interim.

####Manual Installation
Download the [aar artifact](artifacts/fluxxan.aar) in the [artifacts](artifacts/) directory and copy it into the libs directory of your app module.
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
        compile(name: 'fluxxan', ext: 'aar')
        ...
    }
```
##Introduction
 I won't attempt to teach you the concepts of Flux. There are enough articles on the internet for that. Facebook has a great [introduction to flux here](https://facebook.github.io/flux/docs/overview.html) to get you started.
 I will instead focus on showing you how to achieve Flux using Fluxxan. In this introduction tutorial, I will walk you through building the sample Todo app included in the source.

Fluxxan is composed of the
1. State
2. Dispatcher
3. Actions
4. Action Creators
5. Reducers (called stores in traditional Flux)
6. StateListeners

####How it works
You hold your application state in a single `State` object tree. In order to update the `State`, you tell an `ActionCreator`, which creates and dispatches an `Action` object describing what happened. 
The `Dispatcher` calls all registered `Reducer`s with the `State` and `Action ` and each `Reducer` specifies how the `Action` transforms the state tree.
`StateListeners` are passed the returned `State` and update the UI accordingly.

### State
The `State` is the Single Source of Truth of your application. It a single object tree containing the entire app state.

> Unlike Redux, Fluxxan does not force you to use an immutable state. 
> However, doing so is greatly encouraged as it will both improve your code, debugging and increase the overall performance of your application. The state tree can be any object, even a POJO. 
> If you choose not to go Immutable, we've got you covered. A couple of methods are provided to help you short circuit the dispatch process.

For immutability, I've found [Immutables](http://immutables.github.io/) to be a really great way to achieve Immutability. It's quick to setup and understand.

Our Todo app will hold a list of Todo items and a Filter to define which Todos to show. We will use `Immutables` to define our `AppState`.
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

###Actions
`Action`s are objects that define a `Type` and a data `Payload`. Actions in Fluxxan extend [Action](fluxxan/src/main/java/com/umaplay/fluxxan/Action.java).

The action `Type` is a unique string to identify the given action and the `Payload` is any object that you wish to pass to the `Reducer`.

`Action`s are created by `ActionCreator`s and passed to the 	`Dispatcher`.	

###Action Creators
As the name implies, `Action Creators` are methods that create and dispatch `Actions`. 

>Fluxxan does not dictate how you create your `Action Creators`. You have full freedom in this regard. They can be static methods or instance methods. 

We have some guidelines which you can follow. 
1. Create a class to group related action creators. e.g. in our app we will create a `TodoActionCreator` class.
2. Use instances of each creator rather than static methods.
3. Each creator should have a nested `Creator` class that handles the actual creation of the `Action`s using static methods. This gives our `Action Creator`s two roles and allows to dispatch multiple actions or async tasks while still ensuring we can test our `Action`s easily. 
4. Nested creator classes should be composed of pure static methods.  They should return the same `Action` each time when given the same arguments. They should cause no side effects.
5. Don't limit your creators to only dispatch `Actions`. They are an opportunity to centralize all actions that are taken from the ui. e.g. In a music player app, you can have a `PlayerActionCreator` class that has a  `play()` method that tells the media player to start playing and does not dispatch an action. Technically, this is not an `Action Creator` but it's nice that we can have all interactions with the player in one single place.

Let's see what we have in our `Todo` app (simplified for brevity).
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
We extend [BaseActionCreator](fluxxan/src/main/java/com/umaplay/fluxxan/impl/BaseActionCreator.java) which gives us `dispatch(Action)`.

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
...
```

###Todo
  - Writing Tests
  - Performance Tuning
  - Documentation

	
### License
The MIT License
