package com.muralex.worldnews.app.di

import android.content.Context
import com.muralex.worldnews.app.utils.NetworkHelper
import com.muralex.worldnews.app.utils.ResourceProvider
import com.muralex.worldnews.app.utils.SettingsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideSettingsHelper(@ApplicationContext appContext: Context) : SettingsHelper {
        return SettingsHelper(appContext)
    }

    @Provides
    fun provideResourceProvider(@ApplicationContext appContext: Context) : ResourceProvider {
        return ResourceProvider.Base(appContext)
    }

    @Provides
    fun provideNetworkHelper(@ApplicationContext appContext: Context) : NetworkHelper {
        return NetworkHelper(appContext)
    }


}