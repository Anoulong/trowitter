
package com.anou.prototype.core.repository

import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.user.UserEntity
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.prototype.core.strategy.RemoteDataFirstStrategy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Repository that handles TextEntity objects. Contains business logic.
 * Repository modules are responsible should handle data operations.
 * By ensuring this, Repository modules can provide a clean API to the rest of the app and simplify the job of the consumer ViewModel.
 * Repository modules should know where to get the data from and what API calls to make when data is updated if necessary.
 * They can be considered as mediators between different data sources (REST services, Databases, XML files, …etc).
 */
class LoginRepository(
        val applicationDatabase: ApplicationDatabase,
        val apiService: ApiService,
        private val networkService: NetworkConnectivityService
) : BaseRepository() {

    fun getUser() = object : RemoteDataFirstStrategy<UserEntity>() {
        override fun isRemoteAvailable(): Boolean = networkService.getConnectionType() != NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET

        override suspend fun fetchData(): Deferred<UserEntity> = apiService.fetchUser()

        override suspend fun readData(): Deferred<UserEntity> = CompletableDeferred(applicationDatabase.userDao().getUser())

        override suspend fun writeData(data: UserEntity) {
            applicationDatabase.userDao().insert(data)
        }
    }.asLiveData()

    fun deleteByModuleEid(moduleEid: String) {
        applicationDatabase.aboutDao().deleteByModuleEid(moduleEid)
    }
}
