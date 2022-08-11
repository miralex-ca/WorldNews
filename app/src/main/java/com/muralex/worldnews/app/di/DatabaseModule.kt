package com.muralex.worldnews.app.di

import android.content.Context
import androidx.room.Room
import com.muralex.worldnews.data.database.AppDatabase
import com.muralex.worldnews.data.database.FavoriteDao
import com.muralex.worldnews.data.database.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context) : AppDatabase {
        return  Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun providesDao(database: AppDatabase) : NewsDao {
        return database.newsDAO
    }


    @Singleton
    @Provides
    fun providesFavoriteDao(database: AppDatabase) : FavoriteDao {
        return database.favoriteDao
    }
}