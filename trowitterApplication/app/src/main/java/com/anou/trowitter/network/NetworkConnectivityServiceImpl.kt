package com.anou.trowitter.network

import com.anou.prototype.core.service.NetworkConnectivityService
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class NetworkConnectivityServiceImpl : NetworkConnectivityService {

    private val connectionTypeObservable = BehaviorSubject.createDefault(NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET)
    private val connectionTypeChannel = Channel<NetworkConnectivityService.ConnectionType>()

    override fun setConnectionType(connectionType: NetworkConnectivityService.ConnectionType) {
        connectionTypeObservable.onNext(connectionType)
        GlobalScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT) {
            connectionTypeChannel.send(connectionType)
        }
    }

    override fun getConnectionTypeObservable(): Observable<NetworkConnectivityService.ConnectionType> {
        return connectionTypeObservable
    }

    override fun getConnectionType(): NetworkConnectivityService.ConnectionType {
        connectionTypeObservable.value?.let {
            return it
        }
        return NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET
    }

    override fun receiveConnectionTypeChannel(): Channel<NetworkConnectivityService.ConnectionType> {
        return connectionTypeChannel
    }
}