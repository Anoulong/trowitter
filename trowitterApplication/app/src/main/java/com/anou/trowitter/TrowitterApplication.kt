package com.anou.trowitter

import org.koin.android.ext.android.startKoin
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.anou.trowitter.di.modules
import com.facebook.stetho.Stetho


class TrowitterApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin(this, modules)

        // Stetho
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this) // install multidex
    }

}
