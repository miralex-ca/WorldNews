package com.muralex.worldnews.app.utils

import android.content.Context
import androidx.annotation.StringRes
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.Constants.DataErrors

interface ResourceProvider {

    fun getString(@StringRes resId: Int) : String
    fun getErrorMessage(errorType: DataErrors) : String

    class Base (private val context: Context) : ResourceProvider {

        override fun getString(resId: Int) : String = context.getString(resId)

        override fun getErrorMessage(errorType: DataErrors) = getString (
            when (errorType) {
                DataErrors.CONNECTION -> R.string.error_msg_connection_error
                DataErrors.REQUEST -> R.string.error_msg_request_error
                DataErrors.SERVER -> R.string.error_msg_service_error
                else -> R.string.error_msg_generic_error
            }
        )
    }
}