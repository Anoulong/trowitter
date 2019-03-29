package com.anou.trowitter.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import com.anou.prototype.core.usecase.LoginUseCase
import com.anou.prototype.core.viewmodel.LoginViewModel
import com.anou.trowitter.base.BaseActivity
import com.anou.trowitter.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {
    val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.getLocalUser().observe(this, Observer { usecases ->
            usecases?.let {
                when (usecases) {
                    is LoginUseCase.navigateToMainScreen -> {

                        ActivityNavigator(this@SplashActivity).navigate(
                            ActivityNavigator(this@SplashActivity).createDestination()
                                .setIntent(Intent(this@SplashActivity, MainActivity::class.java)), null, null, null
                        )
                        this@SplashActivity.finish()

                    }
                    is LoginUseCase.navigateToLoginScreen -> {


                        ActivityNavigator(this@SplashActivity).navigate(
                            ActivityNavigator(this@SplashActivity).createDestination()
                                .setIntent(Intent(this@SplashActivity, LoginActivity::class.java)), null, null, null
                        )
                        this@SplashActivity.finish()

                    }
                    else -> {
                        ActivityNavigator(this@SplashActivity).navigate(
                            ActivityNavigator(this@SplashActivity).createDestination()
                                .setIntent(Intent(this@SplashActivity, LoginActivity::class.java)), null, null, null
                        )
                        this@SplashActivity.finish()
                    }
                }
            }
        })
    }

}
