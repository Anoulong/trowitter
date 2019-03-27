package com.anou.prototype.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

open class BaseViewModel :  ViewModel() {
    protected var viewModelJob : Job? = null

    init {
        this.viewModelJob = Job()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
    companion object {
        val TAG = BaseViewModel::class.java.simpleName
    }
}