package com.anou.trowitter.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anou.trowitter.lifecycle.CoroutineLifecycleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class BaseActivity : AppCompatActivity() {
    protected val activityLifecycle = CoroutineLifecycleObserver()
    protected val activityScope: CoroutineScope = CoroutineScope(Dispatchers.Main + activityLifecycle.job)
    private var activitySubscriptions: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add observer so any jobs are automatically cancelled
        lifecycle.addObserver(activityLifecycle)
        Log.d(BaseActivity::class.java.simpleName, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        if (activitySubscriptions == null) {
            activitySubscriptions = CompositeDisposable()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearObservables()
    }

    /**
     * Handle Rx Java subscription
     *
     * @param subscription
     */
    protected fun addDisposable(subscription: Disposable) {
        activitySubscriptions?.add(subscription)
    }

    protected fun clearObservables() {
        activitySubscriptions?.let {
            if (!activitySubscriptions!!.isDisposed) {
                activitySubscriptions?.dispose()
            }
        }
    }
}