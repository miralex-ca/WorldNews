package com.muralex.worldnews.presentation.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.ThemeHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val nightModePref = preferenceManager.findPreference<ListPreference>(
            getString(R.string.dark_mode_key)
        )

        nightModePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _: Preference?, value: Any? ->
                lifecycleScope.launch {
                    delay(200)
                    ThemeHelper.setThemeOption(requireContext(), value.toString())
                }
                true
            }
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        savedInstanceState: Bundle?,
    ): RecyclerView {
        val list = super.onCreateRecyclerView(inflater, parent,
            savedInstanceState)
        ViewCompat.setNestedScrollingEnabled(list, false)
        return list
    }
}