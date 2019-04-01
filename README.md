Prototype Android Project
===================================


![](mvvm.png)

### Projects

This project represent MVVM architecture with a high awareness of separation of concern, especially between UI component (Activity/Fragment) and ViewModel by using Sealed Class in Kotlin.
The intention for this demo project is to capture the logic in the Repository for networking and local storage with different strategies. 
These strategies are simple class that use coroutine to perform remote data fetching and local saving into local database. 
Repositories will executed the data gathering tasks  and return different wrapped result to the viewmodel. 
The Viewmodel will take care of the business logic and invoke a proper use cases. Those use cases will be represented as a Sealed Class. 
Those use cases (Sealed Class) will allow the UI to implement action or state that is required to interact with users.


#### Credentials 
batman@yopmail.com/trov

### Architecture Components : MVVM

* **[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)** 

### Sources

- [Kotlin](https://developer.android.com/kotlin/)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)
- [Coroutine](https://youtu.be/EOjq4OIWKqM?t=1302)
- [Sealed Class](https://thoughtbot.com/blog/finite-state-machines-android-kotlin-good-times)


License
-------

By anoulong@gmail.com# trowitter
