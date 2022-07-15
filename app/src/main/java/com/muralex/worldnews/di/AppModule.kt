package com.muralex.worldnews.di

import android.content.Context
import com.muralex.worldnews.presentation.utils.NetworkHelper
import com.muralex.worldnews.presentation.utils.SettingsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideSettingsHelper(@ApplicationContext appContext: Context) : SettingsHelper {
        return SettingsHelper(appContext)
    }

    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext appContext: Context): NetworkHelper {
        return NetworkHelper(appContext)
    }


}