package com.anou.trowitter.ui.setting

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseActivity
import com.anou.trowitter.databinding.ActivitySettingBinding
import com.anou.trowitter.navigation.SettingRouter
import kotlinx.android.synthetic.main.activity_setting.*
import org.koin.android.ext.android.inject


class SettingActivity : BaseActivity() {
    val settingRouter: SettingRouter  by inject()

    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setSupportActionBar(settingToolbar)
        buttonLogout.setOnClickListener({
            //todo delete user from local db
            settingRouter.onBackClicked(this@SettingActivity)
        })
    }

    private fun bind(): View {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        return binding.root
    }
}
