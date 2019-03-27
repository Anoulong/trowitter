package com.anou.prototype.core.controller

import kotlinx.coroutines.channels.Channel

/*******************************************************************************
 * QuickSeries® Publishing inc.
 * <p>
 * Copyright (c) 1992-2017 QuickSeries® Publishing inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of QuickSeries®
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with QuickSeries® and QuickSeries's Partners.
 * <p>
 * Created by Anou Chanthavong on 2019-01-07.
 ******************************************************************************/
interface ApplicationController {
    fun receiveErrorChannel(): Channel<String>
    suspend fun sendErrorChannel(message: String)

}