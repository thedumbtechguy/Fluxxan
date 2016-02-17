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

    allprojects {
        repositories {
            ...
            flatDir { dirs 'libs' }
        }
    }
    
Specify Fluxxan as dependency in your app's gradle file.

    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        compile(name: 'fluxxan', ext: 'aar')
        ...
    }

##Documentation
 I won't attempt to teach you the concepts of Flux. There are enough articles on the internet for that. Instead, I will focus on showing you how to achieve it using Fluxxan. Facebook has a great [introduction to flux here](https://facebook.github.io/flux/docs/overview.html).

### State
The State  is the Single Source of Truth of your application. It a single object tree containing the entire app state.
In order to change the state, you call an action, which dispatches a `Payload` object describing what happened.
Your `Reducer`s specify how each action transforms the state tree.

The above concepts are borrowed from Redux.

> Unlike Redux, Fluxxan does not force you to use an immutable state. However, doing so is greatly encouraged as it will both improve your code, debugging and increase the overall performance of your application. The state tree can be any object, even a POJO. It's your choice.

However, I'd like to stress again that making your state immutable will make your life that much easier. If you choose not to, we've got you covered. A couple of methods are provided to help you short circuit the dispatch process.
Personally, I've found [Immutables](http://immutables.github.io/) to be a really great way to achieve Immutability. It's quick to setup and understand.

...

###Todo
  - Writing Tests
  - Performance Tuning
  - Documentation

	
### License
The MIT License
