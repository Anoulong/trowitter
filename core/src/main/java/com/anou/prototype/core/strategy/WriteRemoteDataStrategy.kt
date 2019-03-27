package com.anou.prototype.core.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class WriteRemoteDataStrategy<T>(val value: T, mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined), liveData: MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, liveData = liveData) {

    override fun start() = writeRemote()

    private fun writeRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                val task = withContext(remoteScope.coroutineContext) { writeDataRemote(value) }
                val data = task.await()

                localScope.launch {
                    dataSaved(data)
                }
                liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = true, strategy = this@WriteRemoteDataStrategy::class))
            } catch (error: Throwable) {
                liveData.postValue(ResourceWrapper(error = error, status = ResourceStatus.ERROR, localData = true, strategy = this@WriteRemoteDataStrategy::class))
            }
        } else {
            liveData.postValue(ResourceWrapper(value = value, error = IllegalStateException("Remote value is not available"), status = ResourceStatus.INVALID, localData = false, strategy = this@WriteRemoteDataStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun writeDataRemote(data: T): Deferred<T>

    @WorkerThread
    open fun dataSaved(data: T) {}
}
