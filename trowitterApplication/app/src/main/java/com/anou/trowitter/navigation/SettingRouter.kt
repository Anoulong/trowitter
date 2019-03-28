package com.anou.trowitter.navigation

import android.content.Intent
import androidx.navigation.ActivityNavigator
import com.anou.trowitter.ui.login.LoginActivity
import com.anou.trowitter.ui.setting.SettingActivity

class SettingRouter {

    fun logout(settingActivity: SettingActivity) {
        ActivityNavigator(settingActivity).navigate(
            ActivityNavigator(settingActivity).createDestination()
                .setIntent(Intent(settingActivity, LoginActivity::class.java)), null, null, null
        )
        settingActivity.finish()
    }
}