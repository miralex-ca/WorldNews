package com.muralex.worldnews.app.utils

import android.content.Context
import androidx.annotation.StringRes
import com.muralex.worldnews.R
import com.muralex.worldnews.app.utils.Constants.DATA_ERRORS

interface ResourceProvider {

    fun getString(@StringRes resId: Int) : String
    fun getErrorMessage(errorType: DATA_ERRORS) : String

    class Base (private val context: Context) : ResourceProvider {

        override fun getString(resId: Int) : String = context.getString(resId)

        override fun getErrorMessage(errorType: DATA_ERRORS) = getString (
            when (errorType) {
                DATA_ERRORS.CONNECTION -> R.string.error_msg_connection_error
                DATA_ERRORS.REQUEST -> R.string.error_msg_request_error
                DATA_ERRORS.SERVER -> R.string.error_msg_service_error
                else -> R.string.error_msg_generic_error
            }
        )
    }
}