package com.anou.trowitter.controller

import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.user.UserEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

class ApplicationControllerImpl() : ApplicationController {
    override var currentUser: UserEntity = UserEntity("", "", "", "", "", "", "", "")

    var errorChannel = Channel<String>()

    override fun receiveErrorChannel(): Channel<String> {
        return errorChannel
    }

    override suspend fun sendErrorChannel(message: String) {
        errorChannel.send(message)
    }

}