package com.anou.prototype.core.repository

import androidx.lifecycle.LiveData
import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.user.UserEntity
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.prototype.core.strategy.*
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
    val networkService: NetworkConnectivityService,
    val applicationController: ApplicationController
) : BaseRepository() {

    fun getRemoteUser(email : String, password : String) = object : RemoteDataValidationStrategy<UserEntity>() {
        override fun isRemoteAvailable(): Boolean = true

        override fun isValid(data : UserEntity): Boolean {
            return email.equals(data.email) && password.equals(data.password)
        }

        override suspend fun fetchData(): Deferred<UserEntity> = apiService.fetchUser()

        override suspend fun writeData(data: UserEntity) {
            applicationDatabase.userDao().insert(data)
        }

    }.asLiveData()

    fun getLocalUser() = object : LocalDataOnlyStrategy<UserEntity>() {

        override suspend fun readData(): Deferred<UserEntity> =
            CompletableDeferred(applicationDatabase.userDao().getUser())

    }.asLiveData()

    fun deleteLocalUser(userEntity: UserEntity): LiveData<ResourceWrapper<UserEntity>> =
        object : WriteLocalDataStrategy<UserEntity>(value = userEntity) {
            override suspend fun writeData(data: UserEntity): Deferred<UserEntity> {
                applicationDatabase.userDao().delete()
                applicationController.currentUser = UserEntity("", "", "", "", "", "", "", "")
                return CompletableDeferred(applicationController.currentUser)
            }
        }.asLiveData()
}
