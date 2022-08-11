package com.muralex.worldnews.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muralex.worldnews.data.model.db.NewsDBData

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(news: List<NewsDBData>)

    @Query ("DELETE FROM news_table")
    suspend fun deleteAllNews()

    @Query("SELECT * from news_table")
    suspend fun getAllNews(): List<NewsDBData>

}
