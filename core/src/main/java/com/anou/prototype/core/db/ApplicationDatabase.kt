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

package com.anou.prototype.core.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.anou.prototype.core.db.about.AboutDao
import com.anou.prototype.core.db.about.AboutEntity
import com.anou.prototype.core.db.module.ModuleDao
import com.anou.prototype.core.db.module.ModuleEntity


/**
 * Main database description.
 */
@Database(
    entities = [
        ModuleEntity::class,  AboutEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    companion object {
        val DATABASE_NAME = "trowitter.db"
    }

    abstract fun moduleDao(): ModuleDao
    abstract fun aboutDao(): AboutDao

}
