package com.anou.trowitter.ui.login

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseActivity
import com.anou.trowitter.databinding.ActivityLoginBinding
import com.anou.trowitter.databinding.ActivityMainBinding
import com.anou.trowitter.utils.Constants
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity() {
    val applicationController: ApplicationController by inject()
    val networkConnectivityService: NetworkConnectivityService by inject()

    lateinit var binding: ActivityLoginBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setSupportActionBar(loginToolbar)

        appBarConfiguration = AppBarConfiguration.Builder()
            .setFallbackOnNavigateUpListener {
                super.onSupportNavigateUp()
            }.build()

        navController = Navigation.findNavController(this, R.id.loginNavigationHost)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onResume() {
        super.onResume()
        navController.navigate(R.id.loginFragmentDestination, null, null)
    }

    private fun bind(): View {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        return binding.root
    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, appBarConfiguration)

}
