package com.muralex.worldnews.app.di

import com.muralex.worldnews.app.utils.NetworkHelper
import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.database.FavoriteDao
import com.muralex.worldnews.data.database.NewsDao
import com.muralex.worldnews.data.model.api.NewsMapper
import com.muralex.worldnews.data.model.db.FavoriteToArticleMapper
import com.muralex.worldnews.data.model.db.NewsDBMapper
import com.muralex.worldnews.data.repository.*
import com.muralex.worldnews.domain.repository.FavoriteRepository
import com.muralex.worldnews.domain.repository.Repository
import com.muralex.worldnews.app.utils.SettingsHelper
import com.muralex.worldnews.data.repository.cachedatasource.NewsCacheDataSource
import com.muralex.worldnews.data.repository.cachedatasource.NewsCacheDataSourceImpl
import com.muralex.worldnews.data.repository.localdatasource.NewsLocalDataSource
import com.muralex.worldnews.data.repository.localdatasource.NewsLocalDataSourceImpl
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSource
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        newsApiService: NewsApiService,
        settingsHelper: SettingsHelper,
        networkHelper: NetworkHelper
    ) : NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(
            apiService = newsApiService,
            mapper = NewsMapper(),
            settingsHelper = settingsHelper,
            networkHelper = networkHelper
        )
    }

    @Singleton
    @Provides
    fun provideNewsCacheDataSource() : NewsCacheDataSource {
        return NewsCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideNewsLocalDataSource(dao: NewsDao, mapper: NewsDBMapper) : NewsLocalDataSource {
        return NewsLocalDataSourceImpl(dao, mapper)
    }

    @Singleton
    @Provides
    fun provideRepository(
        newsCacheDataSource: NewsCacheDataSource,
        newsLocalDataSource: NewsLocalDataSource,
        newsRemoteDataSource: NewsRemoteDataSource,
    ) : Repository {
        return RepositoryImpl(newsCacheDataSource, newsLocalDataSource, newsRemoteDataSource)
    }

    @Singleton
    @Provides
    fun providesFavoriteRepository(
        favoriteDao: FavoriteDao,
        mapper: FavoriteToArticleMapper
    ) : FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteDao, mapper)
    }
}