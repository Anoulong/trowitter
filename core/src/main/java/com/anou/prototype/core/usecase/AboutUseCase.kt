package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.about.AboutEntity

sealed class AboutUseCase {
    class SetData(val about : AboutEntity): AboutUseCase()
    class ShowError(val errorMessage : String): AboutUseCase()
    object ShowLoading: AboutUseCase()
    object HideLoading: AboutUseCase()

}