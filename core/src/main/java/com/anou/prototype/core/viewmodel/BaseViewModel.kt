package com.anou.prototype.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job

open class BaseViewModel :  ViewModel() {
    protected var viewModelSubscriptions = CompositeDisposable()
    protected var viewModelJob : Job? = null

    init {
        this.viewModelJob = Job()
    }

    protected fun addDisposable(subscription: Disposable) {
        viewModelSubscriptions.add(subscription)
    }


    override fun onCleared() {
        super.onCleared()
        if (!viewModelSubscriptions.isDisposed) {
            viewModelSubscriptions.dispose()
        }
        viewModelJob?.cancel()
        Log.d(TAG, "cancel")
    }

    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }
}