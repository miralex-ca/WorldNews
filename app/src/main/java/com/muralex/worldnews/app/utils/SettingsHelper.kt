package com.muralex.worldnews.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.muralex.worldnews.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SettingsHelper (private val context: Context) {

    private var settings: SharedPreferences? = null

    init {
        settings = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getSelectedCountry(): String {
        val default = context.getString(R.string.countries_options_value_default)
        var timeOption = default
        settings?.let {
            timeOption = it.getString(context.getString(R.string.selected_country_key), default).toString()
        }
        return timeOption
    }

    fun getSelectedCountryIndex(): Int {
        val selectedCode = getSelectedCountry()
        val options = context.resources.getStringArray(R.array.countries_options_values)
        return options.indexOf(selectedCode)
    }

    fun setSelectedCountry(country: String)  {
        val sharedPref = settings ?: return
        with (sharedPref.edit()) {
            putString( context.getString(R.string.selected_country_key), country)
            apply()
        }
    }

    fun getCountryName() : String {
        val selected = getSelectedCountry()
        val values = context.resources.getStringArray(R.array.countries_options_values)
        val index = values.indexOf(selected)
        val names = context.resources.getStringArray(R.array.countries_options_text)
        return if (index in names.indices) names[index] else ""
    }

    fun isStartRefreshEnabled() : Boolean {
        val default = true
        return settings?.getBoolean( context.getString(R.string.start_refresh_key), default) ?: default
    }

    fun isSwipeDownEnabled() : Boolean {
        val default = true
        return settings?.getBoolean( context.getString(R.string.swipe_refresh_key), default) ?: default
    }

    fun getLastUpdateCountry() : String {
        val default = ""
        var country = default
        settings?.let {
            country = it.getString(context.getString(R.string.updated_country_key), default).toString()
        }
        return country
    }

    fun setLastUpdateCountry(country: String)  {
        val sharedPref = settings ?: return
        with (sharedPref.edit()) {
            putString( context.getString(R.string.updated_country_key), country)
            apply()
        }
    }

}
