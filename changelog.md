
## ChangeLog

#### 1.0.0
  - Removed coupling of `Fluxxan` and `StateListener` to `ActionCreator`.
  - Changed interface signatures of `StateListener` to remove `ActionCreator`
  - Removed methods related to `ActionCreator` on `Fluxxan`
  - Added `Fluxxan.inject(ActionCreator)` convenience method

#### 0.1.1
  - Fixed bug in `ThreadUtils.getId`
  
#### 0.1.0
  - Completely overhauled the library.
  - Renamed to Fluxxan.
  - Introduced concepts from Redux

#### 0.0.3
  - Added queue and background thread to handle dispatches off the main thread to increase UI performance.
  - Store listener notification is no longer done on the main thread. UI changes need to be explicitly executed on the main thread.
  - Added `ThreadUtils.runOnMain` helper to help run UI updates on the main thread from `onChanged` method
  - Added `StoreListenerFragment` and `StoreListenerView` base classes
  - Renamed `StoreActivity` to `StoreListenerActivity`
  - Added `Dispatcher.start` and `Dispatcher.stop` methods

#### 0.0.2
  - Notify store listeners on the UI thread
  - Added `getFlux` method to `Store`
  - Synchronized `Dispatcher.dispatch` method

#### 0.0.1
  - Initial Code Commit

