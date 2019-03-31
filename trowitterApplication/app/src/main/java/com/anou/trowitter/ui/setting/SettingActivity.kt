package com.anou.trowitter.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import com.anou.prototype.core.usecase.LoginUseCase
import com.anou.prototype.core.usecase.LogoutUseCase
import com.anou.prototype.core.viewmodel.LoginViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseActivity
import com.anou.trowitter.databinding.ActivitySettingBinding
import com.anou.trowitter.navigation.SettingRouter
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.ui.login.LoginActivity
import com.anou.trowitter.utils.DrawableUtils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_dialog_new_tweet.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingActivity : BaseActivity() {
    val loginViewModel by viewModel<LoginViewModel>()
    val settingRouter: SettingRouter  by inject()

    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setSupportActionBar(settingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       supportActionBar?.setDisplayShowTitleEnabled(true)
        settingToolbar.setNavigationOnClickListener { finish() }
        settingToolbar.navigationIcon = DrawableUtils.getTintedDrawable(
            this@SettingActivity,
            R.drawable.ic_action_close,
            R.color.black
        )

        buttonLogout.setOnClickListener {
            loginViewModel.deleteLocalUser().observe(this, Observer { usecases ->
                usecases?.let {
                    when (usecases) {
                        is LogoutUseCase.navigateToLoginScreen -> {
                            settingRouter.logout(this@SettingActivity)
                        }
                    }
                }
            })
        }
    }

    private fun bind(): View {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        return binding.root
    }
}
