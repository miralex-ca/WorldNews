package com.muralex.worldnews.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muralex.worldnews.R

class CountryDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_country_title))
            .setSingleChoiceItems(
                context?.resources?.getStringArray(R.array.countries_options_text),
                arguments?.getInt(SELECTED) ?: 3
            ) { _, checked ->
                val bundle = bundleOf(CHECKED to checked)
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
                dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> dismiss() }
            .create()
    }

    companion object {
        const val REQUEST_KEY = "country_dialog"
        const val TAG = "country"
        const val CHECKED = "checked"
        const val SELECTED = "selected"
    }

}