package com.anou.trowitter.network

import com.anou.prototype.core.service.NetworkConnectivityService
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/*******************************************************************************
 * QuickSeries® Publishing inc.
 *
 *
 * Copyright (c) 1992-2017 QuickSeries® Publishing inc.
 * All rights reserved.
 *
 *
 * This software is the confidential and proprietary information of QuickSeries®
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with QuickSeries® and QuickSeries's Partners.
 *
 *
 * Created by Anou Chanthavong on 2018-01-29.
 */
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