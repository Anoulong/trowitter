package com.anou.prototype.core.strategy

import android.app.admin.SystemUpdatePolicy
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.*

abstract class RemoteDataValidationStrategy<T>(
    mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    localScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    liveData: MediatorLiveData<ResourceWrapper<T>> = MediatorLiveData()
) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData) {

    override fun start(): Job = askRemote()

    private fun askRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(ResourceWrapper(status = ResourceStatus.FETCHING, localData = false, strategy = this@RemoteDataValidationStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                if (isValid(data)) {
                    liveData.postValue(ResourceWrapper(value = data, status = ResourceStatus.SUCCESS, localData = false, strategy = this@RemoteDataValidationStrategy::class))
                    withContext(localScope.coroutineContext) { writeData(data) }
                } else {
                    liveData.postValue(ResourceWrapper(error = IllegalArgumentException("Invalid Credentials"), status = ResourceStatus.ERROR, localData = false, strategy = this@RemoteDataValidationStrategy::class))
                }
            } catch (error: Throwable) {
                withContext(localScope.coroutineContext) { onRemoteFailed(error) }
            }
        }
    }


    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isValid(data: T): Boolean = false

    @WorkerThread
    open suspend fun onRemoteFailed(error: Throwable) {}

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
