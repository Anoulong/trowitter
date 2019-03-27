package com.anou.prototype.core.repository

import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.feature.FeatureEntity
import kotlinx.coroutines.Deferred

class FeatureRepository (
        val applicationDatabase: ApplicationDatabase,
        val apiService: ApiService
) : BaseRepository() {


    fun loadFeatures():  Deferred<List<FeatureEntity>> {
//         val result = MediatorLiveData<List<ModuleEntity>>()

        return apiService.fetchFeatures()
    }



}