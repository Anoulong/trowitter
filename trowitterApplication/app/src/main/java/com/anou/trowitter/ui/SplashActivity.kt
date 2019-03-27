package com.anou.trowitter.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.ActivityNavigator
import com.anou.trowitter.base.BaseActivity
import com.anou.trowitter.databinding.ActivityMainBinding
import com.anou.prototype.core.viewmodel.MainViewModel

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityNavigator(this).navigate(ActivityNavigator(this).createDestination()
                .setIntent(Intent(this, MainActivity::class.java)), null, null, null)
        finish()
    }
}
