
package com.anou.prototype.core.db.about

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AboutDao {
    @Query("SELECT * FROM About WHERE id LIKE :id")
    abstract fun getAbout(id: String): AboutEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(about: AboutEntity)

    @Query("DELETE FROM About WHERE id LIKE :id")
    abstract fun deleteByModuleEid(id: String): Int


}
