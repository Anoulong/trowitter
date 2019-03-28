package com.anou.trowitter.navigation

import com.anou.trowitter.ui.setting.SettingActivity

class SettingRouter {

    fun onBackClicked(settingActivity: SettingActivity) {
        settingActivity.finish()
    }
}