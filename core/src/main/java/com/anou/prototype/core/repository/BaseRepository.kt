package com.anou.prototype.core.repository

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job


open class BaseRepository {
    protected var repositorySubscriptions = CompositeDisposable()
    protected var repositoryJob : Job? = null

    init {
        this.repositoryJob = Job()
        Log.d(TAG, "init repositoryJob")
    }

    fun onJobCancelled() {
        repositoryJob?.cancel()
        Log.d(TAG, "onJobCancelled")
    }

    protected fun addDisposable(subscription: Disposable) {
        repositorySubscriptions.add(subscription)
    }

    fun clearObservables() {
        Log.d(TAG, "$TAG cleared")
        if (!repositorySubscriptions.isDisposed) {
            repositorySubscriptions.dispose()
        }
        repositoryJob?.cancel()
        Log.d(TAG, "cancelled")
    }

    companion object {
        val TAG = BaseRepository::class.java.simpleName
    }
}