package com.anou.prototype.core.controller

import kotlinx.coroutines.channels.Channel

interface ApplicationController {
    fun receiveErrorChannel(): Channel<String>
    suspend fun sendErrorChannel(message: String)

}