package com.muralex.worldnews.di

import android.app.Application
import androidx.preference.PreferenceManager
import com.muralex.worldnews.R
import com.muralex.worldnews.presentation.utils.ThemeHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setThemeOptions()
    }

    private fun setThemeOptions() {
        val settings =  PreferenceManager.getDefaultSharedPreferences(this)
        val darkMode: String? = settings.getString(getString(R.string.dark_mode_key), getString(R.string.dark_mode_disabled))
        darkMode?.let { ThemeHelper.setThemeOption(this, it) }
    }
}