package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.ModuleEntity

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
 * Created by Anou Chanthavong on 2019-02-02.
 ******************************************************************************/
sealed class SideMenuUseCase {
    class SetData(val modules : MutableList<ModuleEntity>): SideMenuUseCase()
    class InitializeModule(val module : ModuleEntity): SideMenuUseCase()
    class ShowError(val errorMessage : String): SideMenuUseCase()
    class ShowSuccess(val successMessage : String): SideMenuUseCase()
    class ShowEmpty(val emptyMessage : String): SideMenuUseCase()
    object ShowLoading: SideMenuUseCase()
    object HideLoading: SideMenuUseCase()

}