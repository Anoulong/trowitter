
package com.anou.prototype.core.repository

import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.about.AboutEntity
import com.anou.prototype.core.strategy.RemoteDataFirstStrategy
import com.anou.prototype.core.service.NetworkConnectivityService

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Repository that handles TextEntity objects. Contains business logic.
 * Repository modules are responsible should handle data operations.
 * By ensuring this, Repository modules can provide a clean API to the rest of the app and simplify the job of the consumer ViewModel.
 * Repository modules should know where to get the data from and what API calls to make when data is updated if necessary.
 * They can be considered as mediators between different data sources (REST services, Databases, XML files, …etc).
 */
class AboutRepository(
        val applicationDatabase: ApplicationDatabase,
        val apiService: ApiService,
        private val networkService: NetworkConnectivityService
) : BaseRepository() {

    companion object {
        private val TAG = AboutRepository::class.java.simpleName
    }

    fun getAbout() = object : RemoteDataFirstStrategy<AboutEntity>() {
//        override fun isRemoteAvailable(): Boolean = networkService.getConnectionType() != NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET
        override fun isRemoteAvailable() = true

        override suspend fun fetchData(): Deferred<AboutEntity> = apiService.fetchAbout()

        override suspend fun readData(): Deferred<AboutEntity> = CompletableDeferred(applicationDatabase.aboutDao().getAbout())

        override suspend fun writeData(data: AboutEntity) {
            applicationDatabase.aboutDao().insert(data)
        }
    }.asLiveData()

    fun deleteByModuleEid() {
        applicationDatabase.aboutDao().deleteByModuleEid()
    }
}
