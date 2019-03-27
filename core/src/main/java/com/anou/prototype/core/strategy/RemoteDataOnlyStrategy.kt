package com.anou.prototype.core.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class RemoteDataOnlyStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData : MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, remoteScope = remoteScope, liveData = liveData) {

    override fun start(): Job = askRemote()

    private fun askRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.FETCHING, localData = false, strategy = this@RemoteDataOnlyStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = false, strategy = this@RemoteDataOnlyStrategy::class))
            } catch (error: Throwable) {
                liveData.postValue(ResourceWrapper(error = error, status = ResourceStatus.ERROR, localData = false, strategy = this@RemoteDataOnlyStrategy::class))
            }
        } else {
            liveData.postValue(ResourceWrapper(error = IllegalStateException("Remote data is not available"), status = ResourceStatus.ERROR, localData = false, strategy = this@RemoteDataOnlyStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>
}
