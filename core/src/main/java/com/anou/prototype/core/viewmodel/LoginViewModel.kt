package com.anou.prototype.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anou.prototype.core.common.Constants
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.user.UserEntity
import com.anou.prototype.core.repository.LoginRepository
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import com.anou.prototype.core.usecase.LaunchUseCase
import com.anou.prototype.core.usecase.LoginUseCase
import com.anou.prototype.core.usecase.LogoutUseCase
import java.util.regex.Pattern

/**
 * ViewModel interacts with model and also prepares observable(s) for News that can be observed by a View.
 * ViewModel can optionally provide hooks for the view to pass events to the model.
 * One of the important implementation strategies of this layer is to decouple it from the View, i.e,
 * ViewModel should not be aware about the view who is interacting with,
 * but aware of the (caller views) Fragments or Activities lifecycle.
 */
class LoginViewModel constructor(
    val applicationController: ApplicationController,
    val loginRepository: LoginRepository
) : BaseViewModel() {
    val loginUseCaseLiveData = MediatorLiveData<LoginUseCase>()

    /**
     * Here is where email and password validation is performed
     */
    fun loginUser(email: String, password: String) {

        val loginLiveData = MutableLiveData<LoginUseCase>()
            loginLiveData.value = LoginUseCase.ShowLoading

            val liveLoginSource = Transformations.switchMap(loginLiveData) {
                loginRepository.getRemoteUser(email, password)
            }

            loginUseCaseLiveData.addSource(liveLoginSource) { result ->
                when (result.status) {
                    ResourceStatus.LOADING,
                    ResourceStatus.FETCHING -> {
                        loginUseCaseLiveData.value = LoginUseCase.ShowLoading
                    }
                    ResourceStatus.SUCCESS -> {
                        result.value?.let { data ->
                            applicationController.currentUser = data
                            loginUseCaseLiveData.value = LoginUseCase.navigateToMainScreen(data)
                        }
                        loginUseCaseLiveData.value = LoginUseCase.HideLoading
                    }
                    ResourceStatus.ERROR -> {
                        result.error?.message?.let { errorMessage ->
                            loginUseCaseLiveData.value = LoginUseCase.ShowError(errorMessage)
                        }
                        loginUseCaseLiveData.value = LoginUseCase.HideLoading
                    }
                    ResourceStatus.UNKNOWN,
                    ResourceStatus.INVALID -> {
                        loginUseCaseLiveData.value = LoginUseCase.ShowError("Something weird here, should never happened")
                        loginUseCaseLiveData.value = LoginUseCase.HideLoading
                    }
                }
            }
    }

    fun getLocalUser(): LiveData<LaunchUseCase> {
        val liveSource = MutableLiveData<LaunchUseCase>()
        val liveUseCase = MediatorLiveData<LaunchUseCase>()
        liveSource.value = LaunchUseCase.navigateToLoginScreen

        val source = Transformations.switchMap(liveSource) {
            loginRepository.getLocalUser()
        }

        liveUseCase.addSource(source) { result ->
            when (result.status) {
                ResourceStatus.LOADING,
                ResourceStatus.FETCHING -> {
                }
                ResourceStatus.SUCCESS -> {
                    if (result.value?.let { data ->
                            liveUseCase.value = LaunchUseCase.navigateToMainScreen(data)
                            applicationController.currentUser = data
                        } == null) {
                        liveUseCase.value = LaunchUseCase.navigateToLoginScreen
                    }
                }
                ResourceStatus.ERROR,
                ResourceStatus.UNKNOWN,
                ResourceStatus.INVALID -> {
                    liveUseCase.value = LaunchUseCase.ShowError("Something weird here, should never happened")
                }
            }
        }

        return liveUseCase
    }

    fun deleteLocalUser(): LiveData<LogoutUseCase> {
        val liveSource = MutableLiveData<LogoutUseCase>()
        val liveUseCase = MediatorLiveData<LogoutUseCase>()
        liveSource.value = LogoutUseCase.navigateToLoginScreen

        val source = Transformations.switchMap(liveSource) {
            loginRepository.deleteLocalUser(applicationController.currentUser)
        }

        liveUseCase.addSource(source) { result ->
            when (result.status) {
                ResourceStatus.SUCCESS -> {
                    liveUseCase.value = LogoutUseCase.navigateToLoginScreen
                }
                ResourceStatus.LOADING,
                ResourceStatus.FETCHING,
                ResourceStatus.ERROR,
                ResourceStatus.UNKNOWN,
                ResourceStatus.INVALID -> {

                }
            }
        }

        return liveUseCase
    }

    override fun onCleared() {
        super.onCleared()
        loginRepository.onJobCancelled()
    }


    fun isValidEmail(email: String): Boolean {
        return Pattern.compile(Constants.EMAIL_ADDRESS).matcher(email).matches()
    }

    fun isValidPassword(password: String, mode: PasswordValidation): Boolean {
        when (mode) {
            PasswordValidation.ValidateAll -> return Pattern.compile(Constants.PASSWORD_CHARS).matcher(password).matches() &&
                    Pattern.compile(Constants.PASSWORD_COUNT).matcher(password).matches() &&
                    Pattern.compile(Constants.PASSWORD_SEQUENCE).matcher(password).matches() &&
                    Pattern.compile(Constants.PASSWORD_SPECIALS).matcher(password).matches()
            PasswordValidation.ValidatedChars -> return Pattern.compile(Constants.PASSWORD_CHARS).matcher(password).matches()
            PasswordValidation.ValidatedCount -> return Pattern.compile(Constants.PASSWORD_COUNT).matcher(password).matches()
            PasswordValidation.ValidatedSequence -> return Pattern.compile(Constants.PASSWORD_SEQUENCE).matcher(password).matches()
            PasswordValidation.ValidatedSpecials -> return Pattern.compile(Constants.PASSWORD_SPECIALS).matcher(password).matches()
        }
        return false
    }

    enum class PasswordValidation {
        ValidatedChars,
        ValidatedCount,
        ValidatedSpecials,
        ValidatedSequence,
        ValidateAll
    }
}
