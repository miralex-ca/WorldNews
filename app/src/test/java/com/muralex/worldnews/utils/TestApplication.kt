package com.muralex.worldnews.utils

import android.app.Application
import com.muralex.worldnews.R

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }
}