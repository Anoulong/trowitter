package com.anou.prototype.core.repository

import android.util.Log
import kotlinx.coroutines.Job


open class BaseRepository {
    protected var repositoryJob : Job? = null

    init {
        this.repositoryJob = Job()
        Log.d(TAG, "init repositoryJob")
    }

    fun onJobCancelled() {
        repositoryJob?.cancel()
        Log.d(TAG, "onJobCancelled")
    }


    companion object {
        val TAG = BaseRepository::class.java.simpleName
    }
}