
package com.anou.prototype.core.db.about

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AboutDao {
    @Query("SELECT * FROM About")
    abstract fun getAbout(): AboutEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(about: AboutEntity)

    @Query("DELETE FROM About")
    abstract fun deleteByModuleEid(): Int


}
