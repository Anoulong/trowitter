
package com.anou.prototype.core.service

import io.reactivex.Observable
import kotlinx.coroutines.channels.Channel

/**
 * Using the native component such as Broadcast Receiver, this will allows to observe network status change.
 * Every time the network switch from Wifi to mobile to offline or vice versa, the observable will call onNext()
 * that will trigger a status change. So the Observer will react to the status and behave in consequence,
 * such as re-fetching the remote data then update the local database and reloading the ui with the fresh new dataset.
 */
interface NetworkConnectivityService {

    fun setConnectionType(connectionType: ConnectionType)
    fun getConnectionType(): ConnectionType
    fun getConnectionTypeObservable(): Observable<ConnectionType>
    fun receiveConnectionTypeChannel(): Channel<ConnectionType>

    enum class ConnectionType {
        TYPE_WIFI,
        TYPE_MOBILE,
        TYPE_NO_INTERNET
    }
}
