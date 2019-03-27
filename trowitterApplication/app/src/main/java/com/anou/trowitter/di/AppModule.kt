package com.anou.trowitter.di

import androidx.room.Room
import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.repository.CategoryRepository
import com.anou.prototype.core.repository.FeatureRepository
import com.anou.prototype.core.repository.ModuleRepository
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.prototype.core.viewmodel.MainViewModel
import com.anou.trowitter.BuildConfig
import com.anou.trowitter.controller.ApplicationControllerImpl
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.network.NetworkConnectivityServiceImpl
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val commonModule = module {
    single { Room.databaseBuilder(androidApplication(), ApplicationDatabase::class.java, ApplicationDatabase.DATABASE_NAME).build() }
    single {
        var client = OkHttpClient.Builder().build()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client = OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(StethoInterceptor())
                    .build()
        }

        Retrofit.Builder()
                .baseUrl(ApiService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()
                .create(ApiService::class.java)
    }

    single { NetworkConnectivityServiceImpl() as NetworkConnectivityService }
    single { ApplicationControllerImpl() as ApplicationController }
    single { MainRouter() }
}

val repositoryModule = module {
    single { ModuleRepository(get(), get(), get()) }
    single { CategoryRepository(get(), get()) }
    single { FeatureRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}

val modules = listOf(commonModule, repositoryModule, viewModelModule)