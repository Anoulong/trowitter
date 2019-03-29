package com.anou.prototype.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anou.prototype.core.db.user.UserEntity
import com.anou.prototype.core.repository.LoginRepository
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.usecase.LoginUseCase

/**
 * ViewModel interacts with model and also prepares observable(s) for News that can be observed by a View.
 * ViewModel can optionally provide hooks for the view to pass events to the model.
 * One of the important implementation strategies of this layer is to decouple it from the View, i.e,
 * ViewModel should not be aware about the view who is interacting with,
 * but aware of the (caller views) Fragments or Activities lifecycle.
 */
class LoginViewModel  constructor(private val loginRepository: LoginRepository) : BaseViewModel() {
    
    fun getUser(): LiveData<LoginUseCase> {
        val liveSource = MutableLiveData<LoginUseCase>()
        val liveUseCase = MediatorLiveData<LoginUseCase>()
        liveSource.value = LoginUseCase.navigateToLoginScreen

        val source = Transformations.switchMap(liveSource) {
            loginRepository.getUser()
        }

        liveUseCase.addSource(source) { result ->
            when (result.status) {
                ResourceStatus.LOADING,
                ResourceStatus.FETCHING -> {
                    liveUseCase.value = LoginUseCase.ShowLoading
                }
                ResourceStatus.SUCCESS -> {
                    result.value?.let { data ->
                        liveUseCase.value = LoginUseCase.navigateToMainScreen(data)
                    }
                    liveUseCase.value = LoginUseCase.HideLoading
                }
                ResourceStatus.ERROR -> {
                    result.error?.message?.let { errorMessage ->
                        liveUseCase.value = LoginUseCase.ShowError(errorMessage)
                    }
                    liveUseCase.value = LoginUseCase.HideLoading
                }
                ResourceStatus.UNKNOWN,
                ResourceStatus.INVALID -> {
                    liveUseCase.value = LoginUseCase.ShowError("Something weird here, should never happened")
                    liveUseCase.value = LoginUseCase.HideLoading
                }
            }
        }

        return liveUseCase
    }

    override fun onCleared() {
        super.onCleared()
        loginRepository.onJobCancelled()
    }
}
