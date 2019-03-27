package com.anou.prototype.core.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.*

abstract class LocalDataAwareStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData : MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, liveData = liveData) {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.LOADING, localData = true, strategy = this@LocalDataAwareStrategy::class))
                val task: Deferred<LiveData<T>> = withContext(localScope.coroutineContext) { readData() }
                val data: LiveData<T> = task.await()
                CoroutineScope(Dispatchers.Main).launch {
                    liveData.addSource(data) { value ->
                        liveData.postValue(ResourceWrapper(value = value, status = ResourceStatus.SUCCESS, localData = true, strategy = this@LocalDataAwareStrategy::class))
                    }
                }
            } catch (error: Throwable) {
                liveData.postValue(ResourceWrapper(error = error, status = ResourceStatus.ERROR, localData = true, strategy = this@LocalDataAwareStrategy::class))
            }
        } else {
            liveData.value = ResourceWrapper(error = IllegalStateException("Local value is not available"), status = ResourceStatus.ERROR, localData = true, strategy = this@LocalDataAwareStrategy::class)
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun readData(): Deferred<LiveData<T>>
}
