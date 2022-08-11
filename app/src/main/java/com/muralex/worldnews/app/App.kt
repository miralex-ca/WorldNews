package com.muralex.worldnews.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.muralex.worldnews.BuildConfig
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.ThemeHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setThemeOptions()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setThemeOptions() {
        val settings =  PreferenceManager.getDefaultSharedPreferences(this)
        val darkMode: String? = settings.getString(getString(R.string.dark_mode_key), getString(R.string.dark_mode_disabled))
        darkMode?.let { ThemeHelper.setThemeOption(this, it) }
    }
}