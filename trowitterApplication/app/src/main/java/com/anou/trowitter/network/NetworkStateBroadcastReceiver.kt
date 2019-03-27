package com.anou.trowitter.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

import android.telephony.TelephonyManager.DATA_CONNECTED
import com.anou.prototype.core.service.NetworkConnectivityService

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
class NetworkStateBroadcastReceiver(private val networkConnectivityService: NetworkConnectivityService?) : BroadcastReceiver() {

    private var networkInfo: NetworkInfo? = null
    private val telephonyListener: TelephonyStateListener

    init {
        telephonyListener = TelephonyStateListener()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (networkConnectivityService != null) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            networkInfo = cm.activeNetworkInfo

            val isConnected = networkInfo != null && networkInfo!!.isConnectedOrConnecting

            if (isConnected) {
                when (networkInfo!!.type) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        tm.listen(telephonyListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE)
                        networkConnectivityService.setConnectionType(NetworkConnectivityService.ConnectionType.TYPE_MOBILE)
                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        tm.listen(telephonyListener, PhoneStateListener.LISTEN_NONE)
                        networkConnectivityService.setConnectionType(NetworkConnectivityService.ConnectionType.TYPE_WIFI)
                    }
                }
            } else {
                tm.listen(telephonyListener, PhoneStateListener.LISTEN_NONE)
                networkConnectivityService.setConnectionType(NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET)
            }
        }
    }

    /**
     * Handle case Mobile connection type to trigger changed
     */
    inner class TelephonyStateListener : PhoneStateListener() {
        override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
            when (state) {
                DATA_CONNECTED -> networkConnectivityService!!.setConnectionType(NetworkConnectivityService.ConnectionType.TYPE_MOBILE)
                else -> networkConnectivityService!!.setConnectionType(NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET)
            }
        }
    }
}
