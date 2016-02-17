
## ChangeLog

####0.1.0
  - Completely overhauled the lbrary.
  - Renamed to Fluxxan.
  - Introduced concepts from Redux

####0.0.3
  - Added queue and background thread to handle dispatches off the main thread to increase UI performance.
  - Store listener notification is no longer done on the main thread. UI changes need to be explicitly executed on the main thread.
  - Added `ThreadUtils.runOnMain` helper to help run UI updates on the main thread from `onChanged` method
  - Added `StoreListenerFragment` and `StoreListenerView` base classes
  - Renamed `StoreActivity` to `StoreListenerActivity`
  - Added `Dispatcher.start` and `Dispatcher.stop` methods

####0.0.2
  - Notify store listeners on the UI thread
  - Added `getFlux` method to `Store`
  - Synchronized `Dispatcher.dispatch` method

####0.0.1
  - Initial Code Commit

