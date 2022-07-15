package com.muralex.worldnews.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.muralex.worldnews.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class SettingsHelper @Inject constructor(
    @ActivityContext private val context: Context,
) {
    private var settings: SharedPreferences? = null

    init {
        settings = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getSelectedCountry(): String {
        val default = context.getString(R.string.countries_options_value_default)
        var timeOption = default
        settings?.let {
            timeOption = it.getString("selected_country", default).toString()
        }
        return timeOption
    }

}
