package com.muralex.worldnews.app.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.muralex.worldnews.R

object ThemeHelper {
    fun setThemeOption(context: Context, option: String) {
        val darkMode: String = option
        val enabledValue = context.getString(R.string.dark_mode_enabled)
        val systemValue = context.getString(R.string.dark_mode_system)
        when (darkMode) {
            systemValue -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            enabledValue -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}