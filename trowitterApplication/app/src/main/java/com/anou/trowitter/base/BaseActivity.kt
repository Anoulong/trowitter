package com.anou.trowitter.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anou.trowitter.lifecycle.CoroutineLifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class BaseActivity : AppCompatActivity(){
    protected val activityLifecycle = CoroutineLifecycleObserver()
    protected val activityScope :CoroutineScope = CoroutineScope(Dispatchers.Main + activityLifecycle.job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add observer so any jobs are automatically cancelled
        lifecycle.addObserver(activityLifecycle)
        Log.d(TAG, "onCreate")
    }

    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }
}