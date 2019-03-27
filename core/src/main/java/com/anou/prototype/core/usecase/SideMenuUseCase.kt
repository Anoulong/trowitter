package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.module.ModuleEntity

sealed class SideMenuUseCase {
    class SetData(val modules : MutableList<ModuleEntity>): SideMenuUseCase()
    class InitializeModule(val module : ModuleEntity): SideMenuUseCase()
    class ShowError(val errorMessage : String): SideMenuUseCase()
    class ShowSuccess(val successMessage : String): SideMenuUseCase()
    class ShowEmpty(val emptyMessage : String): SideMenuUseCase()
    object ShowLoading: SideMenuUseCase()
    object HideLoading: SideMenuUseCase()

}