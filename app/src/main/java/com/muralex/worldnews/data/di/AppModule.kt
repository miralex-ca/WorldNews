package com.muralex.worldnews.data.di

import android.content.Context
import com.muralex.worldnews.BuildConfig
import com.muralex.worldnews.data.api.NewsApiKeyInterceptor
import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.model.utils.NewsToArticleMapper
import com.muralex.worldnews.data.repository.*
import com.muralex.worldnews.domain.Repository
import com.muralex.worldnews.presentation.utils.NetworkHelper
import com.muralex.worldnews.presentation.utils.SettingsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        return loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideNewsApiKeyInterceptor() = NewsApiKeyInterceptor("apiKey", BuildConfig.API_KEY)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: NewsApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder().apply {
                if (BuildConfig.DEBUG)  addInterceptor(loggingInterceptor)
                addInterceptor(apiInterceptor)
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()
    }

    @Singleton
    @Provides
    fun provideApiService(clientHTTP: OkHttpClient): NewsApiService {
        return Retrofit.Builder()
            .client(clientHTTP)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    fun provideSettingsHelper(@ApplicationContext appContext: Context) : SettingsHelper {
        return SettingsHelper(appContext)
    }

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        newsApiService: NewsApiService,
        settingsHelper: SettingsHelper
    ) : NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(newsApiService, NewsToArticleMapper(), settingsHelper)
    }

    @Singleton
    @Provides
    fun provideNewsCacheDataSource() : NewsCacheDataSource {
        return NewsCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideRepository(
        newsCacheDataSource: NewsCacheDataSource,
        newsRemoteDataSource: NewsRemoteDataSource
    ) : Repository {
        return RepositoryImpl(newsCacheDataSource, newsRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext appContext: Context): NetworkHelper {
        return NetworkHelper(appContext)
    }




}