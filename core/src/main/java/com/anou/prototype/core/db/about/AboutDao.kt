
package com.anou.prototype.core.db.about

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import io.reactivex.Flowable

@Dao
abstract class AboutDao {
    @Query("SELECT * FROM About WHERE customModuleEid LIKE :moduleEid")
    abstract fun getAbout(moduleEid: String): AboutEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(about: AboutEntity)

    @Query("DELETE FROM About WHERE customModuleEid LIKE :moduleEid")
    abstract fun deleteByModuleEid(moduleEid: String): Int


}
