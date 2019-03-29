package com.anou.prototype.core.controller

import com.anou.prototype.core.db.user.UserEntity
import kotlinx.coroutines.channels.Channel

interface ApplicationController {
     var currentUser : UserEntity
    fun receiveErrorChannel(): Channel<String>
    suspend fun sendErrorChannel(message: String)

}