package com.muralex.worldnews.data.database

import androidx.room.*
import com.muralex.worldnews.data.model.db.NewsDBData
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsDBData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(news: List<NewsDBData>)

    @Update
    suspend fun updateNews(news: NewsDBData)

    @Delete
    suspend fun delete(news: NewsDBData)

    @Query ("DELETE FROM news_table")
    suspend fun deleteAllNews()

    @Query("SELECT * from news_table")
    suspend fun getAllNews(): List<NewsDBData>


}
