package com.anou.trowitter.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job

/**
 * class DetailFragment : Fragment() {
        private val coroutineLifecycle = CoroutineLifecycleObserver()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            // Add observer so any jobs are automatically cancelled
            lifecycle.addObserver(coroutineLifecycle)
        }

        private fun runTask() {
            // Set the coroutine parent from LifecycleObserver so that it will
            // be automatically cancelled in onStop
            launch(parent = coroutineLifecycle.job) {
            // do something long-running
            }

            launch(parent = coroutineLifecycle.job) {
            // do something else long-running
        }

        // both of these will be cancelled in onStop (if still running)
    }

 * LifecycleObserver which allows easy cancelling of coroutines
 */
class CoroutineLifecycleObserver : LifecycleObserver {
    var job: Job = Job()
        private set

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        // If the job is complete (happens after being previously stopped)
        // lets create a new one
        if (job.isCompleted) {
            job = Job()
            Log.d(TAG, "start")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun cancel() {
        // If the job is active (running) cancel it
        if (job.isActive) {
            job.cancel()
            Log.d(TAG, "cancel")
        }
    }

    companion object {
        val TAG = CoroutineLifecycleObserver::class.java.simpleName
    }
}