package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.user.UserEntity

sealed class LaunchUseCase {
    class navigateToMainScreen(val user: UserEntity) : LaunchUseCase()
    class ShowError(val errorMessage: String) : LaunchUseCase()
    object navigateToLoginScreen : LaunchUseCase()


}