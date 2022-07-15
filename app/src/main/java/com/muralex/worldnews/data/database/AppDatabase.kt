package com.muralex.worldnews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muralex.worldnews.data.model.db.FavoriteData
import com.muralex.worldnews.data.model.db.NewsDBData

@Database(entities = [NewsDBData::class, FavoriteData::class], version = 1,  exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val newsDAO: NewsDao
    abstract val favoriteDao: FavoriteDao

    companion object {
        const val DATABASE_NAME = "news_db"
    }
}





