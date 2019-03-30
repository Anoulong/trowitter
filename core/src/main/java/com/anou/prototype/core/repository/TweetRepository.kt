package com.anou.prototype.core.repository

import androidx.lifecycle.LiveData
import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.module.ModuleEntity
import com.anou.prototype.core.db.tweet.TweetEntity
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.prototype.core.strategy.LocalDataAwareFirstStrategy
import com.anou.prototype.core.strategy.RemoteDataFirstStrategy
import com.anou.prototype.core.strategy.ResourceWrapper
import com.anou.prototype.core.strategy.WriteLocalDataStrategy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class TweetRepository(
        val applicationDatabase: ApplicationDatabase,
        val apiService: ApiService,
        private val networkConnectivityService: NetworkConnectivityService
) : BaseRepository() {

    fun loadTweets(): LiveData<ResourceWrapper<MutableList<TweetEntity>>> = object : RemoteDataFirstStrategy<MutableList<TweetEntity>>() {
        override fun isRemoteAvailable(): Boolean  = true

        override suspend fun fetchData(): Deferred<MutableList<TweetEntity>> = apiService.fetchTweets()

        override suspend fun readData(): Deferred<MutableList<TweetEntity>> = CompletableDeferred(applicationDatabase.tweetDao().loadTweets())

        override suspend fun writeData(data: MutableList<TweetEntity>) {
            applicationDatabase.tweetDao().insertAll(data)
        }
    }.asLiveData()

    /**
     * We should POST to an API endpoint, for the purpose of this test, data will be store locally
     */

    fun insertTweets(tweetEntity: TweetEntity): LiveData<ResourceWrapper<TweetEntity>> = object : WriteLocalDataStrategy<TweetEntity>(value = tweetEntity) {
        override suspend fun writeData(data: TweetEntity): Deferred<TweetEntity> {
            applicationDatabase.tweetDao().insert(data)
            return CompletableDeferred(data)
        }
    }.asLiveData()
}