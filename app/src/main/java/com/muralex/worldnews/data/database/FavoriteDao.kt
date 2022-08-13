package com.muralex.worldnews.data.database

import androidx.room.*
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.FavoriteData
import com.muralex.worldnews.data.model.db.NewsDBData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteData)

    @Query("DELETE from favorite_table WHERE url = :url")
    suspend fun deleteByUrl(url: String)

    @Query("SELECT * from favorite_table")
    fun getAllFavorites(): Flow<List<FavoriteData>>

    @Query("SELECT COUNT(1) FROM favorite_table WHERE url = :url")
    fun checkFavorite(url: String): Flow<Int>

}
