package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.user.UserEntity

sealed class LoginUseCase {
    class navigateToMainScreen(val user: UserEntity) : LoginUseCase()
    class ShowError(val errorMessage: String) : LoginUseCase()
    object navigateToLoginScreen : LoginUseCase()
    object ShowLoading : LoginUseCase()
    object HideLoading : LoginUseCase()


}