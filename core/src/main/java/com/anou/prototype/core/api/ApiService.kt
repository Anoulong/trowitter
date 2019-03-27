/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anou.prototype.core.api

import com.anou.prototype.core.db.ModuleEntity
import com.anou.prototype.core.db.about.AboutEntity
import com.anou.prototype.core.db.category.CategoryEntity
import com.anou.prototype.core.db.feature.FeatureEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 * REST API access points
 */
interface ApiService {
    companion object {
        val URL = "https://raw.githubusercontent.com/Anoulong/android/feature/moduleFragment/api-example/"
    }

    @GET("about.json")
    fun fetchAbout(): Deferred<AboutEntity>

    @GET("modules.json")
    fun fetchModules(): Deferred<MutableList<ModuleEntity>>

    @GET("news.json")
    fun fetchNews(): Deferred<MutableList<CategoryEntity>>

    @GET("https://raw.githubusercontent.com/Anoulong/android/feature/moduleFragment/api-example/categories/faqs.json")
    fun fetchCategory(): Deferred<MutableList<CategoryEntity>>

    @GET("https://raw.githubusercontent.com/Anoulong/android/feature/moduleFragment/api-example/categories/features/faqs.json")
    fun fetchFeatures(): Deferred<MutableList<FeatureEntity>>
}
