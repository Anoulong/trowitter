
package com.anou.prototype.core.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class UserDao {
    @Query("SELECT * FROM User")
    abstract fun getUser(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: UserEntity)

    @Query("DELETE FROM User")
    abstract fun delete(): Int


}
