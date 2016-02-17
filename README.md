# Fluxxan

Fluxxan is an Android implementation of the Flux Architecture that combines concepts from both Fluxxor and Redux.

Originally DroidFlux, it started as a direct Android port of the popular [Fluxxor](http://fluxxor.com) library seeking to implement the [Flux Architecture](https://facebook.github.io/flux/) as popularised by Facebook for Android.
The library has evolved into a hybrid of the original Flux and [Redux](https://github.com/reactjs/redux), borrowing some of the great ideas from Redux while trying to be as close to the original dictates of Flux as much as possible. 
I ended up with something that looks like Flux, but works a lot like Redux.


[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Fluxxan-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1786)

### Current Version: 0.1.0
Starting at 0.1.0 Fluxxan follows [Semantic Versionioning](http://semver.org/).

## Installation

Simply clone the project and open in Android Studio. It contains a sample that illustrates a basic use. You can play around with it.

##Documentation
 I won't attempt to teach you the concepts of Flux. There are enough articles on the internet for that. Instead, I will focus on showing you how to achieve it using Fluxxan.

### State
The State  is the Single Source of Truth of your application. It a single object tree containing the entire app state.
In order to change the state, you call an action, which dispatches a `Payload` object describing what happened.
Your `Reducer`s specify how each action transforms the state tree.

The above concepts are borrowed from Redux.

> Unlike Redux, Fluxxan does not force you to use an immutable state. However, doing so  will both improve your code and increase performance of your application.

A simple state tree can look like this.

...

###Todo
  - Writing Tests
  - Performance Tuning
  - Documentation

	
### License
The MIT License
