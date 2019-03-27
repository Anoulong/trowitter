package com.anou.prototype.core.repository

import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.category.CategoryEntity
import kotlinx.coroutines.Deferred

class CategoryRepository (
        val applicationDatabase: ApplicationDatabase,
        val apiService: ApiService
) : BaseRepository() {


    fun loadCategory():  Deferred<List<CategoryEntity>> {
//         val result = MediatorLiveData<List<ModuleEntity>>()

        return apiService.fetchCategory()
    }



}