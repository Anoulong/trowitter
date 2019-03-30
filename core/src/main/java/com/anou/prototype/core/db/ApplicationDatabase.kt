package com.anou.prototype.core.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anou.prototype.core.common.converter.StringConverter
import com.anou.prototype.core.db.about.AboutDao
import com.anou.prototype.core.db.about.AboutEntity
import com.anou.prototype.core.db.module.ModuleDao
import com.anou.prototype.core.db.module.ModuleEntity
import com.anou.prototype.core.db.tweet.TweetDao
import com.anou.prototype.core.db.tweet.TweetEntity
import com.anou.prototype.core.db.user.UserDao
import com.anou.prototype.core.db.user.UserEntity


/**
 * Main database description.
 */
@Database(
    entities = [
        ModuleEntity::class,  AboutEntity::class,  UserEntity::class,  TweetEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    companion object {
        val DATABASE_NAME = "trowitter.db"
    }

    abstract fun moduleDao(): ModuleDao
    abstract fun aboutDao(): AboutDao
    abstract fun userDao(): UserDao
    abstract fun tweetDao(): TweetDao

}
