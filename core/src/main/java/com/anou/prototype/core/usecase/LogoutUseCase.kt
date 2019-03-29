package com.anou.prototype.core.usecase

sealed class LogoutUseCase {
    object navigateToLoginScreen : LogoutUseCase()
}