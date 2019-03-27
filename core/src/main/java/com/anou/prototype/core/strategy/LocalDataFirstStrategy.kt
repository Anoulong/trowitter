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

abstract class LocalDataFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData : MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData) {

    override fun start(): Job = askLocal()

    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.LOADING, localData = true, strategy = this@LocalDataFirstStrategy::class))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                if (isValid(data)) {
                    liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = true, strategy = this@LocalDataFirstStrategy::class))
                } else {
                    askRemote(data = data, warning = IllegalArgumentException("local value is not valid"))
                }
            } catch (error: Throwable) {
                withContext(remoteScope.coroutineContext) { onLocalFailed(error) }
                askRemote(warning = error)
            }
        } else {
            askRemote(warning = IllegalStateException("Local value is not available"))
        }
    }

    private fun askRemote(warning: Throwable, data: T? = null) = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.FETCHING, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))

                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable) {
                liveData.postValue(ResourceWrapper(error = error, status = ResourceStatus.ERROR, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
            }
        } else {
            liveData.postValue(ResourceWrapper(value = data, error = IllegalStateException("Remote value is not available"), status = ResourceStatus.INVALID, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    open fun onLocalFailed(error: Throwable){}

    @MainThread
    abstract suspend fun isValid(data: T): Boolean

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
