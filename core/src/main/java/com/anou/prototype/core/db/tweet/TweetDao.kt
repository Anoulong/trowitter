package com.anou.prototype.core.db.tweet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anou.prototype.core.db.tweet.TweetEntity

@Dao
abstract class TweetDao {
    @Query("SELECT * FROM Tweet ORDER BY date(createdAt) DESC")
    abstract fun loadTweets(): LiveData<MutableList<TweetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(tweet: TweetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(modules: MutableList<TweetEntity>)


}
