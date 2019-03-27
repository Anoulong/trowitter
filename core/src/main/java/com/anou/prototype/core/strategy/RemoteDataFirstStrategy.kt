package com.anou.prototype.core.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.*

abstract class RemoteDataFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData : MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData) {

    override fun start(): Job = askRemote()

    private fun askRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.FETCHING, localData = false, strategy = this@RemoteDataFirstStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = false, strategy = this@RemoteDataFirstStrategy::class))

                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable ) {
                withContext(localScope.coroutineContext) {  onRemoteFailed(error) }
                askLocal(error)
            }
        } else {
            askLocal(IllegalStateException("Remote data is not available"))
        }
    }

    private fun askLocal(warning: Throwable) = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.LOADING, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
            } catch (error: Throwable) {
                liveData.postValue(ResourceWrapper(error = error, status = ResourceStatus.ERROR, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
            }
        } else {
            liveData.postValue(ResourceWrapper(error = IllegalStateException("Local data is not available"), status = ResourceStatus.INVALID, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    open suspend fun onRemoteFailed(error: Throwable){}

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
