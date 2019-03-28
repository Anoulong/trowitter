package com.anou.prototype.core.db.module

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class ModuleDao {
    @Query("SELECT * FROM Module ORDER BY position ASC")
    abstract fun loadAllModules(): LiveData<MutableList<ModuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(modules: MutableList<ModuleEntity>)
//
//    @Query("SELECT COUNT(*) from Module")
//    abstract fun count(): LiveData<Int>
//
//    @Query("SELECT * FROM Module where eid LIKE  :moduleEid")
//    abstract fun loadModuleByEid(moduleEid: String): LiveData<ModuleEntity>
//
//    @Query("SELECT * FROM Module")
//    abstract fun retrieveAll(): LiveData<List<ModuleEntity>>

}
