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
    }

    fun onJobCancelled() {
        repositoryJob?.cancel()
    }

    protected fun addDisposable(subscription: Disposable) {
        repositorySubscriptions.add(subscription)
    }

    fun clearObservables() {
        if (!repositorySubscriptions.isDisposed) {
            repositorySubscriptions.dispose()
        }
        repositoryJob?.cancel()
    }

    companion object {
        val TAG = BaseRepository::class.java.simpleName
    }
}