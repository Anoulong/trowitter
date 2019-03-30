package com.anou.trowitter.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.anou.prototype.core.db.module.ModuleEntity
import com.anou.trowitter.R
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.ui.setting.SettingActivity
import com.anou.trowitter.utils.Constants
import com.anou.trowitter.ui.tweet.NewTweetFragmentDialog


class MainRouter {

    fun onModuleSelected(mainActivity: MainActivity, module: ModuleEntity, isLaunchModule: Boolean) {
        val navBuilder = NavOptions.Builder()
        val navOptions =
            if (isLaunchModule) navBuilder.setPopUpTo(R.id.loadingFragmentDestination, true).build() else null

        var bundle = Bundle()

        module.let {
            bundle.putString(Constants.MODULE_ID, module.id)
            bundle.putString(Constants.MODULE_TITLE, module.title)

            when (module.type) {
                ModuleEntity.TWEET -> {
                    Navigation.findNavController(mainActivity, R.id.mainNavigationHost)
                        .navigate(R.id.tweetFragmentDestination, bundle, navOptions)
                }
                ModuleEntity.ABOUT -> {
                    Navigation.findNavController(mainActivity, R.id.mainNavigationHost)
                        .navigate(R.id.aboutFragmentDestination, bundle, navOptions)
                }
                ModuleEntity.SETTING -> {
                    ActivityNavigator(mainActivity).navigate(
                        ActivityNavigator(mainActivity).createDestination()
                            .setIntent(Intent(mainActivity, SettingActivity::class.java)), null, null, null
                    )
                }
                else -> Toast.makeText(mainActivity, module.title, Toast.LENGTH_SHORT).show()
            }
        }

        mainActivity.closeDrawer()
    }

    fun openNewTweetFragment(activity: Activity) {
        activity.let { it as MainActivity
            val dialog = NewTweetFragmentDialog.newInstance()
            dialog.show(it.supportFragmentManager, NewTweetFragmentDialog.TAG)
        }

    }

    fun onFragmentViewed(mainActivity: MainActivity, string: String) {
        println("Log state ==> $string")
        mainActivity.supportActionBar?.title = string
    }
}