package com.muralex.worldnews.app.di

import com.muralex.worldnews.BuildConfig
import com.muralex.worldnews.data.api.NewsApiKeyInterceptor
import com.muralex.worldnews.data.api.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

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
        apiInterceptor: NewsApiKeyInterceptor,
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
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }






}